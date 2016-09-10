package com.boluozhai.snowflake.xgit.site.impl;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.site.XGitSite;

public class XGitSiteImplementation {

	public static XGitSite createSite(SnowflakeContext context) {
		return new XGitSiteImpl(context);
	}

	public static RepositoryLocator createRepoLocator() {
		return new SiteRepositoryLocatorImpl();
	}

}
