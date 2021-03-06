package com.boluozhai.snowflake.xgit.vfs.support;

import java.net.URI;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.MutablePathNode;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.vfs.FileRepository;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.vfs.context.FileRepositoryContext;
import com.boluozhai.snowflake.xgit.workspace.Workspace;

public class FileRepositoryFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return new Builder();
	}

	private class Builder extends FileXGitComponentBuilder {

		@Override
		public Component create(ComponentContext cc) {
			FileRepositoryContext frc = (FileRepositoryContext) cc;
			VPath path = this.getPath();
			return new MyComponent(frc, path);
		}

		@Override
		public void configure(ContextBuilder cb) {

			// NOP

			URI uri = cb.getURI();
			VFS vfs = VFS.Factory.getVFS(cb.getParent());
			VFile repo_dir = vfs.newFile(uri);
			VPath repo_path = repo_dir.toPath();
			String[] keys = cb.getAttributeNames();

			// System.out.println(this + ".config(base):" + uri);

			for (String key : keys) {
				Object attr = cb.getAttribute(key);
				boolean is_path_node = (attr instanceof MutablePathNode);
				boolean is_builder = (attr instanceof ComponentBuilder);
				if (is_path_node && is_builder) {

					final MutablePathNode node = (MutablePathNode) attr;
					final VPath path;

					if (key == null) {
						continue;
					} else if (key.equals(XGitContext.component.repository)) {
						path = repo_path;
					} else if (key.equals(XGitContext.component.working)) {
						path = repo_path.parent();
					} else {
						path = repo_path.child(key);
					}

					// System.out.println(this + ".config(key):" + key);

					node.setPath(path);
				}
			}

		}

	}

	private class MyComponent implements FileRepository {

		private final FileRepositoryContext _context;
		private final VPath _path;
		private Workspace _working;

		public MyComponent(FileRepositoryContext cc, VPath path) {
			this._context = cc;
			this._path = path;
		}

		@Override
		public ComponentContext getComponentContext() {
			return this._context;
		}

		@Override
		public ComponentLifecycle lifecycle() {
			return new Life(this);
		}

		@Override
		public URI location() {
			return _path.file().toURI();
		}

		@Override
		public VFile getFile() {
			return this._path.file();
		}

		@Override
		public FileRepositoryContext context() {
			return this._context;
		}

		@Override
		public Workspace getWorking() {
			Workspace wk = this._working;
			if (wk == null) {
				ComponentContext cc = this.context();
				Config conf = cc.getBean(XGitContext.component.config,
						Config.class);
				String bare = conf.getProperty("core.bare", "false");
				if (bare.equals("true")) {
					return null;
				}
				wk = cc.getBean(XGitContext.component.working, Workspace.class);
				this._working = wk;
			}
			return wk;
		}
	}

	private class Life implements ComponentLifecycle {

		private final MyComponent _repo;

		public Life(MyComponent repo) {
			this._repo = repo;
		}

		@Override
		public void onCreate() {
			this._repo.hashCode();
		}
	}
}
