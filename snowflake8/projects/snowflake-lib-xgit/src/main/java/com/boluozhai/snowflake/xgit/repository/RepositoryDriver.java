package com.boluozhai.snowflake.xgit.repository;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface RepositoryDriver {

	Repository open(SnowflakeContext context, URI uri, RepositoryOption option);

	RepositoryLocator getLocator();

}
