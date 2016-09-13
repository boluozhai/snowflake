package com.boluozhai.snowflake.xgit.http.client;

import java.net.URI;

public interface RepositoryConnection {

	GitHttpClient getClient();

	URI getLocation();

	GitHttpResource getResource(String path);

}
