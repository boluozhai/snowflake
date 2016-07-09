package com.boluozhai.snowflake.xgit.vfs.support;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryDriver;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.vfs.impl.FileRepositoryDriverImpl;

public class FileRepositoryDriver implements RepositoryDriver {

	private final FileRepositoryDriverImpl impl = new FileRepositoryDriverImpl();

	@Override
	public Repository open(SnowContext context, URI uri, RepositoryOption option) {
		return impl.open(context, uri, option);
	}

	@Override
	public RepositoryLocator getLocator() {
		return impl.getLocator();
	}

}
