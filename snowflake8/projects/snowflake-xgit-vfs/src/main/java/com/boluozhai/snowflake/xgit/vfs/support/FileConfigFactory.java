package com.boluozhai.snowflake.xgit.vfs.support;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.xgit.vfs.impl.config.FileRepoConfigImpl;

public class FileConfigFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return FileRepoConfigImpl.newBuilder();
	}

}
