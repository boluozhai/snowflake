package com.boluozhai.snowflake.rootrepo.impl;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rootrepo.RootRepoClient;

public class RootRepoImplementation {

	public static RootRepoClient newClient(SnowflakeContext context,
			String repositoryURL) {
		return RootRepoClientImpl.create(context, repositoryURL);
	}

}
