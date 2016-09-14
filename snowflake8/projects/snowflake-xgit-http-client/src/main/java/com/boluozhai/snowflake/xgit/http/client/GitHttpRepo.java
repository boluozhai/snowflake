package com.boluozhai.snowflake.xgit.http.client;

import java.net.URI;

public interface GitHttpRepo {

	GitHttpClient getClient();

	URI getURI();

	GitHttpResource getResource(String path);

	GitHttpService getService(String path, String service_name);

}
