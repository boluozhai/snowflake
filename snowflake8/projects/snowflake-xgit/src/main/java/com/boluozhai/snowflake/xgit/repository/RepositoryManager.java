package com.boluozhai.snowflake.xgit.repository;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface RepositoryManager {

	Repository open(SnowflakeContext context, URI uri, RepositoryOption option);

	RepositoryDriver getDriver(SnowflakeContext context, URI uri,
			RepositoryOption option);

}
