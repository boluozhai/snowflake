package com.boluozhai.snowflake.rootrepo;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface RootRepoAgent {

	class Factory {

		public static RootRepoAgent getAgent(SnowflakeContext context) {
			Class<RootRepoAgent> type = RootRepoAgent.class;
			String key = type.getName();
			return context.getBean(key, type);
		}

	}

	RootRepoClient getClient(SnowflakeContext context);

}
