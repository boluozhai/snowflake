package com.boluozhai.snowflake.rest.client;

import java.net.URI;

public interface RESTClient {

	RestService getService(URI uri);

}
