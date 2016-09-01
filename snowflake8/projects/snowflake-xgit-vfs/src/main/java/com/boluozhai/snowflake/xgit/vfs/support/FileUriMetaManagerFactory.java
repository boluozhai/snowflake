package com.boluozhai.snowflake.xgit.vfs.support;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.vfs.impl.meta.FileUriMetaManagerImpl;

public class FileUriMetaManagerFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder extends FileXGitComponentBuilder {

		@Override
		public Component create(ComponentContext cc, ContextBuilder cb) {
			return new FileUriMetaManagerImpl(this, cc);
		}

	}

}
