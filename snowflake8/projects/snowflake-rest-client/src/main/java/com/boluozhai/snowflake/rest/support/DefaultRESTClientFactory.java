package com.boluozhai.snowflake.rest.support;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rest.client.RESTClient;
import com.boluozhai.snowflake.rest.client.RESTClientFactory;
import com.boluozhai.snowflake.rest.impl.client.RestClientFactoryImpl;

public class DefaultRESTClientFactory implements RESTClientFactory {

	private final RESTClientFactory impl = new RestClientFactoryImpl();

	@Override
	public RESTClient getClient(SnowflakeContext context) {
		return impl.getClient(context);
	}

}
