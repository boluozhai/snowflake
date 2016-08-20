package com.boluozhai.snowflake.xgit.utils;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;

public class DefaultRepositoryAgent implements RepositoryAgent {

	@Override
	public Repository getRepository(SnowflakeContext context) {

		CurrentLocation loc = CurrentLocation.Factory.get(context);
		URI uri = loc.getLocation(context);

		RepositoryManager xgit = XGit.getRepositoryManager(context);
		return xgit.open(context, uri, null);

	}

}
