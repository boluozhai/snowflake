package com.boluozhai.snow.xgit.repository;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowContext;

public interface RepositoryDriver {

	Repository open(SnowContext context, URI uri, RepositoryOption option);

	RepositoryLocator getLocator();

}
