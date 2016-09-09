package com.boluozhai.snowflake.rest.client;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface RESTClientFactory {

	RESTClient getClient(SnowflakeContext context);

}
