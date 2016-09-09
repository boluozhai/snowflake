package com.boluozhai.snowflake.rest.impl.client;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rest.client.RESTClient;
import com.boluozhai.snowflake.rest.client.RESTClientFactory;

public class RestClientFactoryImpl implements RESTClientFactory {

	@Override
	public RESTClient getClient(SnowflakeContext context) {
		return new ImpRestClient(context);
	}

}
