package com.boluozhai.snowflake.rest.client;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface RestType {

	SnowflakeContext getContext();

	URI getURI();

	RestAPI getOwner();

	String getName();

	RestResource getResource(String id);

}
