package com.boluozhai.snowflake.xgit.vfs.support;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryDriver;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.support.AbstractRepositoryDriver;
import com.boluozhai.snowflake.xgit.support.RepositoryProfile;
import com.boluozhai.snowflake.xgit.vfs.impl.FileRepositoryDriverImpl;

public class FileRepositoryDriver extends AbstractRepositoryDriver implements
		RepositoryDriver {

	private FileRepositoryDriverImpl _impl = null;

	private FileRepositoryDriverImpl getImpl() {
		FileRepositoryDriverImpl impl = _impl;
		if (impl == null) {
			RepositoryProfile pf = this.getProfile();
			_impl = impl = new FileRepositoryDriverImpl(pf);
		}
		return impl;
	}

	@Override
	public Repository open(SnowContext context, URI uri, RepositoryOption option) {
		return this.getImpl().open(context, uri, option);
	}

	@Override
	public RepositoryLocator getLocator() {
		return this.getImpl().getLocator();
	}

}
