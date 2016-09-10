package com.boluozhai.snowflake.xgit.http.support;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.xgit.http.impl.XHttpGitClientImpl;

public class HttpGitClientFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return XHttpGitClientImpl.newBuilder();
	}

}
