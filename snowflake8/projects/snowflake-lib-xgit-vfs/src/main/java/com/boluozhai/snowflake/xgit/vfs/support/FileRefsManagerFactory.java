package com.boluozhai.snowflake.xgit.vfs.support;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.xgit.vfs.impl.refs.FileRefsManagerImpl;

public class FileRefsManagerFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return new FileRefsManagerImpl.Builder();
	}

}
