package com.boluozhai.snowflake.xgit.site;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface XGitSite {

	public class Agent {

		public static XGitSite getSite(SnowflakeContext context) {
			Class<XGitSiteFactory> type = XGitSiteFactory.class;
			String key = type.getName();
			XGitSiteFactory factory = context.getBean(key, type);
			return factory.getSite(context);
		}

	}

	Repository getRootRepository();

	Repository getSystemRepository(String name);

	Repository getUserRepository(String name);

	URI getRootURI();

	URI getSystemURI(String name);

	URI getUserURI(String name);

}
