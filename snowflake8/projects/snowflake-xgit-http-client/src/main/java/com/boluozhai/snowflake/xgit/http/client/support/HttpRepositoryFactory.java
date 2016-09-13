package com.boluozhai.snowflake.xgit.http.client.support;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.xgit.http.client.repo.impl.XHttpRepoImpl;

public class HttpRepositoryFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return XHttpRepoImpl.newBuilder();
	}

}
