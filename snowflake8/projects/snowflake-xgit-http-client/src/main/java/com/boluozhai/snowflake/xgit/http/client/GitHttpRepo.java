package com.boluozhai.snowflake.xgit.http.client;

import java.net.URI;

public interface GitHttpRepo {

	GitHttpClient getClient();

	URI getURI();

	GitHttpResource getResource(String path);

}
