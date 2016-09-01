package com.boluozhai.snowflake.rest.client;

import java.net.URI;

public interface RestService {

	URI getURI();

	String getName();

	RestAPI getAPI(String name);

}
