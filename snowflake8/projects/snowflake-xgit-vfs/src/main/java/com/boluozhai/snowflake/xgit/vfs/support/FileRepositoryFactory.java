package com.boluozhai.snowflake.xgit.vfs.support;

import java.net.URI;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.xgit.vfs.FileRepository;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.vfs.context.FileRepositoryContext;

public class FileRepositoryFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return new Builder();
	}

	private class Builder extends FileXGitComponentBuilder {

		@Override
		public Component create(ComponentContext cc, ContextBuilder cb) {
			FileRepositoryContext frc = (FileRepositoryContext) cc;
			VPath path = this.getPath();
			return new MyComponent(frc, path);
		}

	}

	private class MyComponent implements FileRepository {

		private final FileRepositoryContext _context;
		private final VPath _path;

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
