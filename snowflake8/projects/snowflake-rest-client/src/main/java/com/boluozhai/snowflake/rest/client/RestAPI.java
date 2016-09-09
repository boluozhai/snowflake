package com.boluozhai.snowflake.rest.client;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface RestAPI {

	SnowflakeContext getContext();

	URI getURI();

	String getName();

	RestService getOwner();

	RestType getType(String name);

}
