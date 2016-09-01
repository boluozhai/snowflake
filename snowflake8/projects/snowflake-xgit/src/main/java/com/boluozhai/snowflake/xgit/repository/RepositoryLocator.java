package com.boluozhai.snowflake.xgit.repository;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface RepositoryLocator {

	URI locate(SnowflakeContext context, URI uri, RepositoryOption option);

}
