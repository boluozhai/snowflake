package com.boluozhai.snowflake.httpclient;

import java.io.Closeable;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class HttpConnection extends HttpURLConnection implements
		Closeable {

	protected HttpConnection(URL u) {
		super(u);
	}

}
