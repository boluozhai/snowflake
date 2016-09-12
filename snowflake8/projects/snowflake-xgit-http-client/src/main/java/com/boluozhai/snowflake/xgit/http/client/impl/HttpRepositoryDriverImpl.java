package com.boluozhai.snowflake.xgit.http.client.impl;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryDriver;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.support.AbstractRepositoryDriver;
import com.boluozhai.snowflake.xgit.support.OpenRepositoryParam;
import com.boluozhai.snowflake.xgit.support.RepositoryLoader;
import com.boluozhai.snowflake.xgit.support.RepositoryProfile;

public class HttpRepositoryDriverImpl extends AbstractRepositoryDriver
		implements RepositoryDriver {

	public HttpRepositoryDriverImpl(RepositoryProfile pf) {
		this.setProfile(pf);
	}

	@Override
	public Repository open(SnowflakeContext context, URI uri,
			RepositoryOption option) {

		OpenRepositoryParam param = new OpenRepositoryParam();
		param.context = context;
		param.uri = uri;
		param.option = option;
		param.profile = this.getProfile();

		RepositoryLoader loader = new HttpRepositoryBuilder();
		return loader.load(param);
	}

	@Override
	public RepositoryLocator getLocator() {
		RepositoryProfile prof = this.getProfile();
		return new HttpRepositoryLocatorImpl(prof);
	}

}
