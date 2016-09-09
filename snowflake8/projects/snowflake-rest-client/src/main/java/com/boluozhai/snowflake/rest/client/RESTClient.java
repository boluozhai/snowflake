package com.boluozhai.snowflake.rest.client;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface RESTClient {

	SnowflakeContext getContext();

	RestService getService(URI uri);

}
