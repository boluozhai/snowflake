package com.boluozhai.snowflake.xgit.site.support;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.site.impl.XGitSiteImplementation;
import com.boluozhai.snowflake.xgit.support.AbstractRepositoryDriver;
import com.boluozhai.snowflake.xgit.support.DefaultXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.support.OpenRepositoryParam;
import com.boluozhai.snowflake.xgit.support.RepositoryLoader;

public class SiteRepositoryDriver extends AbstractRepositoryDriver {

	@Override
	public Repository open(SnowflakeContext context, URI uri,
			RepositoryOption option) {

		OpenRepositoryParam param = new OpenRepositoryParam();
		param.context = context;
		param.option = option;
		param.uri = uri;
		param.profile = this.getProfile();
		RepositoryLoader loader = new DefaultXGitComponentBuilder();
		return loader.load(param);
	}

	@Override
	public RepositoryLocator getLocator() {
		return XGitSiteImplementation.createRepoLocator();
	}

}
