package com.boluozhai.snowflake.rest.client;

import java.net.URI;

public interface RestType {

	URI getURI();

	RestAPI getOwnerAPI();

	String getName();

	RestRequest get(String id);

	RestRequest post(String id);

	RestRequest put(String id);

	RestRequest delete(String id);

}
