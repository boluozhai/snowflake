package com.boluozhai.snowflake.xgit.vfs.support;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.xgit.vfs.impl.refs.FileRefsImplementation;

public class FileRefsManagerFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return FileRefsImplementation.newRefsBuilder();
	}

}
