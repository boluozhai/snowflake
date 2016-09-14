package com.boluozhai.snowflake.xgit.http.client;

import java.io.InputStream;

public interface GitHttpEntity {

	long contentLength();

	String contentType();

	InputStream input();

}
