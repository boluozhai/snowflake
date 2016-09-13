package com.boluozhai.snowflake.xgit.http.client;

import java.net.URI;

public interface GitHttpResource {

	interface PATH {

		String info_refs = "info/refs";

	}

	RepositoryConnection getOwner();

	URI getURI();

	String getPath();

	GitHttpService getService(String name);

}
