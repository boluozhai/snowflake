package com.boluozhai.snowflake.rest.client;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;

/******************************************************
 * The REST Service (RestApp) Object
 * */

public interface RestService {

	SnowflakeContext getContext();

	URI getURI();

	String getName();

	RestAPI getAPI(String name);

}
