package com.boluozhai.snowflake.xgit.http.client.repo.impl;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.support.RepositoryProfile;

final class HttpRepositoryLocatorImpl implements RepositoryLocator {

	public HttpRepositoryLocatorImpl(RepositoryProfile prof) {
	}

	@Override
	public URI locate(SnowflakeContext context, URI uri, RepositoryOption option) {
		return uri;
	}

}
