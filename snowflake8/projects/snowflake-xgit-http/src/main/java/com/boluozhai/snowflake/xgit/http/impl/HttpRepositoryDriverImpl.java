package com.boluozhai.snowflake.xgit.http.impl;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryDriver;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.support.AbstractRepositoryDriver;
import com.boluozhai.snowflake.xgit.support.RepositoryProfile;

public class HttpRepositoryDriverImpl extends AbstractRepositoryDriver
		implements RepositoryDriver {

	public HttpRepositoryDriverImpl(RepositoryProfile pf) {
		this.setProfile(pf);
	}

	@Override
	public Repository open(SnowflakeContext context, URI uri,
			RepositoryOption option) {

		final RepositoryProfile pf = this.getProfile();
		final HttpRepositoryBuilder builder;
		builder = new HttpRepositoryBuilder(context);
		builder.profile(pf);
		builder.uri(uri);
		builder.option(option);
		return builder.create();
	}

	@Override
	public RepositoryLocator getLocator() {
		RepositoryProfile prof = this.getProfile();
		return new HttpRepositoryLocatorImpl(prof);
	}

}
