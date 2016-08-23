package com.boluozhai.snowflake.xgit.command;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFSContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.utils.CurrentLocation;

public class GitInit implements CLICommandHandler {

	private interface Path {

		String branches = "branches";
		String hooks = "hooks";
		String info = "info";
		String objects = "objects";
		String refs = "refs";

		String objects_info = "objects/info";
		String objects_pack = "objects/pack";
		String refs_heads = "refs/heads";

		String config = "config";
		String description = "description";
		String HEAD = "HEAD";

	}

	@Override
	public void process(SnowflakeContext context, String command) {

		RepoMaker maker = new RepoMaker();
		maker.init(context);

		maker.addDir(Path.branches);
		maker.addDir(Path.hooks);
		maker.addDir(Path.info);
		maker.addDir(Path.objects);
		maker.addDir(Path.refs);

		maker.addDir(Path.objects_info);
		maker.addDir(Path.objects_pack);
		maker.addDir(Path.refs_heads);

		maker.addFile(Path.config);
		maker.addFile(Path.description);
		maker.addFile(Path.HEAD);

		try {
			maker.create();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static class NewNode {

		private final boolean _is_dir;
		private final String _path;

		private VFile _file;

		public NewNode(boolean isdir, String path) {
			this._is_dir = isdir;
			this._path = path;
		}

		public static NewNode dir(String path) {
			return new NewNode(true, path);
		}

		public static NewNode file(String path) {
			return new NewNode(false, path);
		}

		public void create(VFile base) throws IOException {
			VFile p = base;
			String[] array = _path.split("/");
			for (String name : array) {
				p = p.child(name);
			}
			if (_is_dir) {
				p.mkdirs();
			} else {
				p.createNewFile();
			}
			this._file = p;
		}

		public void writeText(String text) throws IOException {
			OutputStream out = null;
			try {
				VFS vfs = _file.vfs();
				VFSContext context = vfs.context();
				VFSIO io = VFSIO.Agent.getInstance(context);
				out = io.output(_file);
				TextTools.save(text, out);
			} finally {
				IOTools.close(out);
			}
		}

	}

	private static class RepoMaker {

		private boolean _bare;
		private VFile _base;
		private String _name;

		private Map<String, NewNode> nodes;

		public RepoMaker() {
			nodes = new HashMap<String, NewNode>();
		}

		public void addDir(String path) {
			nodes.put(path, NewNode.dir(path));
		}

		public void addFile(String path) {
			nodes.put(path, NewNode.file(path));
		}

		public void init(SnowflakeContext context) {

			CurrentLocation cl = CurrentLocation.Factory.get(context);
			URI uri = cl.getLocation(context);
			VFS vfs = VFS.Factory.getVFS(context);
			VFile base = vfs.newFile(uri);

			String bare = context.getParameter("--bare", null);

			String name = null;
			for (int i = 0; i < 100; i++) {
				String val = context.getParameter("" + i, null);
				if (val == null) {
					break;
				} else if (val.startsWith("-")) {
					// skip
				} else {
					name = val;
					break;
				}
			}

			this._base = base;
			this._bare = (bare != null);
			this._name = name;

		}

		public void create() throws IOException {

			VFile repo_dir = this.makeRepoDir();
			for (NewNode node : this.nodes.values()) {
				node.create(repo_dir);
			}

			NewNode config = nodes.get(Path.config);
			NewNode head = nodes.get(Path.HEAD);

			config.writeText("[core]\n	repositoryformatversion=0");
			head.writeText("ref: refs/heads/master");

			this.makeConfig(config._file);

			String msg = "init empty repository in path of " + repo_dir;
			System.out.println(msg);

		}

		private void makeConfig(VFile path) throws IOException {

			VFS vfs = path.vfs();
			VFSContext context = vfs.context();
			RepositoryManager rm = XGit.getRepositoryManager(context);
			URI uri = path.toURI();
			Repository repo = rm.open(context, uri, null);
			Config config = repo.context().getBean(
					XGitContext.component.config, Config.class);

			config.setProperty("core.bare", String.valueOf(_bare));
			config.setProperty("core.filemode", "true");
			config.setProperty("core.logallrefupdates", "true");

			config.save();

		}

		private VFile makeRepoDir() {

			VFile dir = _base.child(_name);
			if (dir.exists()) {
				String msg = "the file/directory is exists: " + dir;
				throw new RuntimeException(msg);
			}

			if (_bare) {
				return dir;
			} else {
				return dir.child(".git");
			}

		}

	}

}
