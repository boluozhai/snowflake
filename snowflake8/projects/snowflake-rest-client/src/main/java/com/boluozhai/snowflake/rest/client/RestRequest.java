package com.boluozhai.snowflake.rest.client;

import java.net.URI;

public interface RestRequest  extends  RestMessage  {

	URI getURI();

	RestType getType();

	String getId();

	RestResponse execute();

}
