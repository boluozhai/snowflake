package com.boluozhai.snowflake.xgit.repository;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowContext;

public interface RepositoryLocator {

	URI locate(SnowContext context, URI uri, RepositoryOption option);

}
