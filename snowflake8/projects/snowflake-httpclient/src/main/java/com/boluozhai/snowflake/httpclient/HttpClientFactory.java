package com.boluozhai.snowflake.httpclient;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface HttpClientFactory {

	HttpClient getClient(SnowflakeContext context);

}
