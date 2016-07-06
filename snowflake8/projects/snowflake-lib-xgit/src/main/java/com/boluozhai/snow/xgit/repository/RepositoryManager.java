package com.boluozhai.snow.xgit.repository;

import java.net.URI;

import com.boluozhai.snow.context.SnowContext;

public interface RepositoryManager {

	Repository open(SnowContext context, URI uri, RepositoryOption option);

	RepositoryDriver getDriver(SnowContext context, URI uri,
			RepositoryOption option);

}
