package com.boluozhai.snowflake.xgit.http.impl;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryDriver;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.support.AbstractRepositoryDriver;
import com.boluozhai.snowflake.xgit.support.DefaultXGitComponentBuilder;
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
		final DefaultXGitComponentBuilder builder;
		builder = new DefaultXGitComponentBuilder(context);
		builder.setProfile(pf);
		builder.setURI(uri);
		builder.setOption(option);
		final ComponentContext cc = builder.create();
		return cc.getBean(XGitContext.component.repository, Repository.class);
	}

	@Override
	public RepositoryLocator getLocator() {
		RepositoryProfile prof = this.getProfile();
		return new HttpRepositoryLocatorImpl(prof);
	}

}
