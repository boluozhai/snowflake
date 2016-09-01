package com.boluozhai.snowflake.rest.client;

import java.net.URI;

public interface RestAPI {

	URI getURI();

	String getName();

	RestService getOwnerService();

	RestType getType(String name);

}
