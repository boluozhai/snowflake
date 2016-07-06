package com.boluozhai.snow.xgit.repository;

import java.net.URI;

import com.boluozhai.snow.context.SnowContext;

public interface RepositoryLocator {

	URI locate(SnowContext context, URI uri, RepositoryOption option);

}
