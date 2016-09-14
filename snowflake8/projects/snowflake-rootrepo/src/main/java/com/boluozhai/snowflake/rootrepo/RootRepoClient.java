package com.boluozhai.snowflake.rootrepo;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rootrepo.method.DoTest;
import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;

public interface RootRepoClient {

	class Agent {

		public static RootRepoClient getClient(SnowflakeContext context) {
			RootRepoAgent agent = RootRepoAgent.Factory.getAgent(context);
			return agent.getClient(context);
		}

	}

	GitHttpRepo getRootRepository();

	DoTest doTest();

}
