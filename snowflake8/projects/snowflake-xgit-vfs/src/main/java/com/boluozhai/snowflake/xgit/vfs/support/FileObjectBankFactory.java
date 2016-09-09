package com.boluozhai.snowflake.xgit.vfs.support;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.vfs.FileObjectBank;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponent;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.vfs.impl.objectbank.FileObjectBankImpl;

public class FileObjectBankFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return new MyBuilder();
	}

	private static class MyBuilder extends FileXGitComponentBuilder {

		@Override
		public Component create(ComponentContext cc) {

			VFile file = this.getPath().file();
			return new MyComponent(cc, file);

		}

		@Override
		public void configure(ContextBuilder cb) {
			// NOP
		}

	}

	private static class MyComponent extends FileObjectBankImpl implements
			FileXGitComponent, FileObjectBank {

		public MyComponent(ComponentContext cc, VFile file) {
			super(cc, file);
		}

	}

}
