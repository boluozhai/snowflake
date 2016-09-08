package com.boluozhai.snowflake.xgit.site;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface XGitSite {

	public class Agent {

		public static XGitSite getSite(SnowflakeContext context) {
			Class<XGitSiteFactory> type = XGitSiteFactory.class;
			String key = type.getName();
			XGitSiteFactory factory = context.getBean(key, type);
			return factory.getSite(context);
		}

	}

	DataRepository getDataRepository(String name);

	SystemRepository getSystemRepository();

	UserRepository getUserRepository(String name);

	URI getDataRepositoryURI(String name);

	URI getSystemRepositoryURI();

	URI getUserReopsitoryURI(String name);

}
