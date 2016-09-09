package com.boluozhai.snowflake.rest;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rest.client.RESTClient;
import com.boluozhai.snowflake.rest.client.RESTClientFactory;

public class REST {

	public static RESTClient getClient(SnowflakeContext context) {
		Class<RESTClientFactory> type = RESTClientFactory.class;
		String key = type.getName();
		RESTClientFactory factory = context.getBean(key, type);
		return factory.getClient(context);
	}

}
