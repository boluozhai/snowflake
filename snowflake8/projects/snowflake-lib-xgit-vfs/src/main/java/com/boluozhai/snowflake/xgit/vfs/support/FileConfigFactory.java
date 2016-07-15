package com.boluozhai.snowflake.xgit.vfs.support;

import java.io.IOException;

import com.boluozhai.snow.mvc.model.Component;
import com.boluozhai.snow.mvc.model.ComponentBuilder;
import com.boluozhai.snow.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snow.mvc.model.ComponentContext;
import com.boluozhai.snow.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.vfs.FileConfig;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponent;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.vfs.impl.config.FileRepoConfigImpl;

public class FileConfigFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return new MyBuilder();
	}

	private static class MyBuilder extends FileXGitComponentBuilder {

		@Override
		public Component create(ComponentContext cc, ContextBuilder cb) {
			VFile file = this.getPath().file();
			return new MyComponent(cc, file);
		}

	}

	private static class MyComponent implements FileXGitComponent, FileConfig {

		private final ComponentContext _context;
		private final VFile _file;
		private final FileRepoConfigImpl _impl;

		private MyComponent(ComponentContext context, VFile file) {
			this._context = context;
			this._file = file;
			this._impl = new FileRepoConfigImpl();
		}

		@Override
		public VFile getFile() {
			return this._file;
		}

		@Override
		public ComponentLifecycle lifecycle() {
			return new MyLife(this);
		}

		@Override
		public ComponentContext getComponentContext() {
			return this._context;
		}

		@Override
		public void load() throws IOException {
			this._impl.load(this._context, this._file);
		}

		@Override
		public void save() throws IOException {
			this._impl.save(this._context, this._file);
		}

		@Override
		public void setProperty(String key, String value) {
			this._impl.put(key, value);
		}

		@Override
		public String[] getPropertyNames() {
			return this._impl.keys();
		}

		@Override
		public String getProperty(String name) {
			return this._impl.get(name);
		}

		@Override
		public String getProperty(String name, Object defaultValue) {
			return this._impl.get(name, defaultValue);
		}

	}

	private static class MyLife implements ComponentLifecycle {

		public MyLife(MyComponent com) {
		}

		@Override
		public void onCreate() {
		}
	}

}
