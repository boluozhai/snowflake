package com.boluozhai.snowflake.rest.client;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface RestResource {

	SnowflakeContext getContext();

	RestType getOwner();

	URI getURI();

	String getId();

	RestRequest get();

	RestRequest post();

	RestRequest put();

	RestRequest delete();

}
