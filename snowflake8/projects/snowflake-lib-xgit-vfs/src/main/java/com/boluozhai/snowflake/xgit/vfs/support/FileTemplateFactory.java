package com.boluozhai.snowflake.xgit.vfs.support;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponent;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;

public class FileTemplateFactory implements ComponentBuilderFactory {

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

	private static class MyComponent implements FileXGitComponent {

		private final ComponentContext _context;
		private final VFile _file;

		private MyComponent(ComponentContext context, VFile file) {
			this._context = context;
			this._file = file;
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
	}

	private static class MyLife implements ComponentLifecycle {

		public MyLife(MyComponent com) {
		}

		@Override
		public void onCreate() {
		}
	}

}
