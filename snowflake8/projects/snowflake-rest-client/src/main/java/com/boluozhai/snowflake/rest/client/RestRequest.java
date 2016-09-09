package com.boluozhai.snowflake.rest.client;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface RestRequest extends RestMessage {

	SnowflakeContext getContext();

	URI getURI();

	RestResource getOwner();

	RestResponse execute();

}
