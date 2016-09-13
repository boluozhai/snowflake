package com.boluozhai.snowflake.httpclient.support;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.httpclient.HttpClient;
import com.boluozhai.snowflake.httpclient.HttpClientFactory;
import com.boluozhai.snowflake.httpclient.impl.HttpClientImpl;

public class DefaultHttpClientFactory implements HttpClientFactory {

	@Override
	public HttpClient getClient(SnowflakeContext context) {
		return HttpClientImpl.create(context);
	}

}
