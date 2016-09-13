package com.boluozhai.snowflake.xgit.http.client.support;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.http.client.repo.impl.HttpRepositoryDriverImpl;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.support.AbstractRepositoryDriver;
import com.boluozhai.snowflake.xgit.support.RepositoryProfile;

public class HttpRepositoryDriver extends AbstractRepositoryDriver {

	private HttpRepositoryDriverImpl _impl = null;

	private HttpRepositoryDriverImpl getImpl() {
		HttpRepositoryDriverImpl impl = _impl;
		if (impl == null) {
			RepositoryProfile pf = this.getProfile();
			_impl = impl = new HttpRepositoryDriverImpl(pf);
		}
		return impl;
	}

	@Override
	public Repository open(SnowflakeContext context, URI uri,
			RepositoryOption option) {
		return this.getImpl().open(context, uri, option);
	}

	@Override
	public RepositoryLocator getLocator() {
		return this.getImpl().getLocator();
	}

}
