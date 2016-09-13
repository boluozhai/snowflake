package com.boluozhai.snowflake.xgit.http.client;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface GitHttpClientFactory {

	GitHttpClient newClient(SnowflakeContext context);

}
