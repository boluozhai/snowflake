package com.boluozhai.snowflake.xgit.vfs.support;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.xgit.vfs.impl.remotes.FileRemotesImplementation;

public class FileRemoteManagerFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return FileRemotesImplementation.newRemotesBuilder(this);
	}

}
