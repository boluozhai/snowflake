package com.boluozhai.snowflake.xgit.command;

import java.net.URI;
import java.util.Arrays;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitContext;
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
		FileScannerBuilderFactory fsb_factory = FileScannerUtils.getFactory();
		FileScannerBuilder fsb = fsb_factory.newBuilder(xgit_context);
		fsb.setUserDataFactory(new MyUserDataFactory());
		FileScanner scanner = fsb.create(works, add_path);

		// scan
		MyScanContext sc = new MyScanContext(scanner);
		sc.scan();

	}

	private static class MyScanContext {

		private final FileScanner scanner;

		public MyScanContext(FileScanner scanner) {
			this.scanner = scanner;
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

			if (node.isIgnored()) {
				System.out.println("ignore " + node.getName());
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

		private void onFile(ScanningNode node) {

			System.out.println("onFile: " + node.getFile());

			MyUserData parent = (MyUserData) node.parent().getUserData();

			ObjectId id = ObjectId.Factory.create("1234");

			parent.addFile(node, id);
		}

		private void onDirEnd(ScanningNode node) {

			MyUserData ud = (MyUserData) node.getUserData();
			ud.save();

		}

		private void onDirBegin(ScanningNode node) {

			MyUserData ud = (MyUserData) node.getUserData();
			ud.load();

			ScanningNode parent0 = node.parent();
			if (parent0 != null) {
				MyUserData parent = (MyUserData) parent0.getUserData();
				parent.addDir(node);
			}

		}

	}

	private static class MyUserData {

		private final ScanningNode _node;

		public MyUserData(ScanningNode node) {
			this._node = node;
		}

		public void addFile(ScanningNode node, ObjectId id) {
			// TODO Auto-generated method stub

		}

		public void addDir(ScanningNode node) {
			// TODO Auto-generated method stub

		}

		public void load() {
			// TODO Auto-generated method stub

			VFile path = _node.getFile();
			System.out.println("load dir info : " + path.getName());

		}

		public void save() {
			// TODO Auto-generated method stub

			VFile path = _node.getFile();
			System.out.println("save dir info : " + path.getName());

		}

	}

	private static class MyUserDataFactory implements UserDataFactory {

		@Override
		public Object createUserData(ScanningNode node) {
			return new MyUserData(node);
		}
	}

}
