package com.boluozhai.snowflake.xgit.http.client.support;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.http.client.GitHttpClient;
import com.boluozhai.snowflake.xgit.http.client.GitHttpClientFactory;
import com.boluozhai.snowflake.xgit.http.client.impl.GitHttpClientImpl;

public class DefaultGitHttpClientFactory implements GitHttpClientFactory {

	@Override
	public GitHttpClient newClient(SnowflakeContext context) {
		return GitHttpClientImpl.newInstance(context);
	}

}
