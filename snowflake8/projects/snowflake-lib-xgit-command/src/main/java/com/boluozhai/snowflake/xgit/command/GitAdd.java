package com.boluozhai.snowflake.xgit.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.meta.UriMeta;
import com.boluozhai.snowflake.xgit.meta.UriMetaManager;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.PlainId;
import com.boluozhai.snowflake.xgit.pojo.TreeItem;
import com.boluozhai.snowflake.xgit.pojo.TreeObject;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.utils.CurrentLocation;
import com.boluozhai.snowflake.xgit.utils.RepositoryAgent;
import com.boluozhai.snowflake.xgit.vfs.FileWorkspace;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScanner;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScannerBuilder;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScannerBuilderFactory;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScannerUtils;
import com.boluozhai.snowflake.xgit.vfs.scanner.ScanningNode;
import com.boluozhai.snowflake.xgit.vfs.scanner.UserDataFactory;

public class GitAdd implements CLICommandHandler {

	@Override
	public void process(SnowContext context, String command) {

		// repository
		RepositoryAgent repo_agent = RepositoryAgent.Factory.get(context);
		Repository repo = repo_agent.getRepository(context);
		XGitContext xgit_context = repo.context();

		// workspace
		FileWorkspace works = xgit_context.getBean(
				XGitContext.component.workspace, FileWorkspace.class);
		VFile wk_dir = works.getFile();
		VFS vfs = wk_dir.vfs();

		// location
		CurrentLocation cur_loc = CurrentLocation.Factory.get(context);
		URI location = cur_loc.getLocation(context);
		VFile add_path = vfs.newFile(location);

		// scanner
		MyUserDataFactory user_data_factory = new MyUserDataFactory();
		FileScannerBuilderFactory fsb_factory = FileScannerUtils.getFactory();
		FileScannerBuilder fsb = fsb_factory.newBuilder(xgit_context);
		fsb.setUserDataFactory(user_data_factory);
		FileScanner scanner = fsb.create(works, add_path);

		// scan
		MyScanContext sc = new MyScanContext(scanner);
		user_data_factory._scan_context = sc;
		sc.scan();

	}

	private static class MyScanContext {

		private final FileScanner scanner;
		private UriMetaManager _uri_meta_man;
		private VFSIO _vfs_io;
		private ObjectBank _obj_bank;

		public MyScanContext(FileScanner scanner) {
			this.scanner = scanner;
		}

		public UriMetaManager get_meta_manager() {
			UriMetaManager manager = this._uri_meta_man;
			if (manager == null) {
				ComponentContext cc = scanner.getWorkspace()
						.getComponentContext();
				manager = cc.getBean(XGitContext.component.uri_meta,
						UriMetaManager.class);
				this._uri_meta_man = manager;
			}
			return manager;
		}

		public void scan() {
			ScanningNode node = scanner.getBaseNode();
			this.scan(node, 128);
		}

		private void scan(final ScanningNode node, final int depth_limit) {

			final VFile file = node.getFile();

			if (depth_limit < 0) {
				String msg = "the path is too deep: " + file;
				throw new RuntimeException(msg);
			}

			if (!this.accept(node)) {
				System.out.println("    ignore " + node.getName());
				return;
			}

			if (file.isDirectory()) {
				this.onDirBegin(node);
				String[] list = file.list();
				Arrays.sort(list);
				for (String name : list) {
					ScanningNode ch = node.child(name);
					this.scan(ch, depth_limit - 1);
				}
				this.onDirEnd(node);
			} else {
				this.onFile(node);
			}

		}

		private boolean accept(ScanningNode node) {
			if (node.isIgnored()) {
				return false;
			}
			if (node.isInRepository()) {
				return false;
			}
			if (!node.isInWorkspace()) {
				return false;
			}
			return true;
		}

		private void onFile(ScanningNode node) {

			final String name = node.getName();
			final VFile file = node.getFile();
			final MyUserData parent = (MyUserData) node.parent().getUserData();

			// System.out.println("    " + name);

			final long length_2 = file.length();
			final long last_mod_2 = file.lastModified();
			final int mode_2 = file.canExecute() ? TreeItem.MODE.executable
					: TreeItem.MODE.normal;

			ObjectId id = null;
			TreeItem item = parent.getItem(name);

			if (item != null) {
				// check
				final long length_1 = item.getSize();
				final long last_mod_1 = item.getLastModified();
				id = PlainId.convert(item.getId());
				if ((last_mod_1 != last_mod_2) || (length_1 != length_2)) {
					item = null;
				} else if (!this.is_object_exists(id)) {
					item = null;
				}
			}

			if (item == null) {
				// re-make-blob
				id = this.make_blob(file, length_2);
				item = new TreeItem();
				item.setId(PlainId.convert(id));
				item.setName(name);
				item.setLastModified(last_mod_2);
				item.setSize(length_2);
			}

			item.setMode(mode_2);
			parent.add(item);
		}

		private ObjectId make_blob(VFile file, long length) {
			InputStream in = null;
			OutputStream out = null;
			try {
				VFSIO io = this.get_vfs_io();
				in = io.input(file);

				ObjectBank bank = this.get_object_bank();
				String type = GitObject.TYPE.blob;
				GitObjectBuilder builder = bank.newBuilder(type, length);
				out = builder.getOutputStream();

				IOTools.pump(in, out);
				out.close();
				out = null;

				GitObject obj = builder.create();
				return obj.id();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				IOTools.close(in);
				IOTools.close(out);
				System.out.println("add blob " + file.getName());
			}
		}

		private boolean is_object_exists(ObjectId id) {
			if (id == null) {
				return false;
			}
			ObjectBank bank = this.get_object_bank();
			GitObject obj = bank.object(id);
			return obj.exists();
		}

		private VFSIO get_vfs_io() {
			VFSIO io = this._vfs_io;
			if (io == null) {
				ComponentContext cc = this.get_object_bank()
						.getComponentContext();
				io = VFSIO.Agent.getInstance(cc);
				this._vfs_io = io;
			}
			return io;
		}

		private ObjectBank get_object_bank() {
			ObjectBank bank = this._obj_bank;
			if (bank == null) {
				FileWorkspace works = this.scanner.getWorkspace();
				ComponentContext cc = works.getComponentContext();
				String key = XGitContext.component.objects;
				bank = cc.getBean(key, ObjectBank.class);
				this._obj_bank = bank;
			}
			return bank;
		}

		private void onDirEnd(ScanningNode node) {
			try {
				System.out.println(log_dir_tab + node.getFile());
				MyUserData ud = (MyUserData) node.getUserData();
				ud.save();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private static final String log_dir_tab = "        ";

		private void onDirBegin(ScanningNode node) {
			System.out.println(log_dir_tab + node.getFile());
			MyUserData ud = (MyUserData) node.getUserData();
			ud.load();
			ScanningNode parent = node.parent();
			if (parent != null) {
				MyUserData parent_ud = (MyUserData) parent.getUserData();
				final int mode = TreeItem.MODE.directory;
				final String name = node.getName();
				final TreeItem item = new TreeItem();
				item.setName(name);
				item.setMode(mode);
				parent_ud.add(item);
			}
		}

	}

	private static class MyUserData {

		private final MyScanContext _scan_context;
		private final ScanningNode _node;

		private Map<String, TreeItem> _old_items;
		private Map<String, TreeItem> _new_items;
		private UriMeta _meta_info;

		public MyUserData(MyScanContext scan_context, ScanningNode node) {
			this._scan_context = scan_context;
			this._node = node;
		}

		public TreeItem getItem(String name) {
			return this._old_items.get(name);
		}

		public void add(TreeItem item) {
			this._new_items.put(item.getName(), item);
		}

		private UriMeta get_meta_info() {
			UriMeta meta_info = this._meta_info;
			if (meta_info == null) {
				URI uri = this._node.getFile().toURI();
				UriMetaManager meta_man = this._scan_context.get_meta_manager();
				meta_info = meta_man.getMeta(this.meta_type(), uri);
				this._meta_info = meta_info;
			}
			return meta_info;
		}

		private Class<TreeObject> meta_type() {
			return TreeObject.class;
		}

		public void load() {
			Map<String, TreeItem> items = null;
			try {
				UriMeta meta = this.get_meta_info();
				TreeObject tree = meta.loadJSON(this.meta_type());
				items = tree.getItems();
			} catch (Exception e) {
				// NOP
				// System.err.println(e);
			} finally {
				if (items == null) {
					items = new HashMap<String, TreeItem>();
				}
				this._old_items = items;
				this._new_items = new HashMap<String, TreeItem>();
			}
		}

		public void save() throws IOException {
			UriMeta meta = this.get_meta_info();
			TreeObject tree = new TreeObject();
			tree.setItems(_new_items);
			meta.saveJSON(tree);
		}

	}

	private static class MyUserDataFactory implements UserDataFactory {

		private MyScanContext _scan_context;

		@Override
		public Object createUserData(ScanningNode node) {
			MyScanContext scan_context = this._scan_context;
			return new MyUserData(scan_context, node);
		}

	}

}
