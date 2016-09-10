package com.boluozhai.snowflake.xgit.site;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface XGitSite {

	interface SITE_URI {

		String system = "site:/system";
		String data = "site:/data/";
		String user = "site:/user/";

	}

	public class Agent {

		public static XGitSite getSite(SnowflakeContext context) {
			Class<XGitSiteFactory> type = XGitSiteFactory.class;
			String key = type.getName();
			XGitSiteFactory factory = context.getBean(key, type);
			return factory.getSite(context);
		}

	}

	SystemRepository getSystemRepository();

}
