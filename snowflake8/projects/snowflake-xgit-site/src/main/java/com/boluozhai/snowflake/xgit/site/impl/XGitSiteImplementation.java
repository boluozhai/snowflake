package com.boluozhai.snowflake.xgit.site.impl;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.site.XGitSite;
import com.boluozhai.snowflake.xgit.support.RepositoryLoader;

public class XGitSiteImplementation {

	public static XGitSite createSite(SnowflakeContext context) {
		return new XGitSiteImpl(context);
	}

	public static RepositoryLocator createRepoLocator() {
		return new SiteRepositoryLocatorImpl();
	}

	public static RepositoryLoader createRepoLoader() {
		return SiteRepoLoaderImpl.newLoader();
	}

	public static ComponentBuilder newSystemRepoBuilder() {
		return SystemRepoImpl.newBuilder();
	}

	public static ComponentBuilder newDataRepoBuilder() {
		return DataRepoImpl.newBuilder();
	}

	public static ComponentBuilder newUserRepoBuilder() {
		return UserRepoImpl.newBuilder();
	}

	public static ComponentBuilder newPartitionRepoBuilder() {
		return PartitionRepoImpl.newBuilder();
	}

}
