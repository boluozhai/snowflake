package com.boluozhai.snowflake.httpclient.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.httpclient.HttpClient;
import com.boluozhai.snowflake.httpclient.HttpConnection;

public class HttpClientImpl implements HttpClient {

	private HttpClientImpl(SnowflakeContext context) {
	}

	public static HttpClient create(SnowflakeContext context) {
		return new HttpClientImpl(context);
	}

	@Override
	public HttpConnection open(URI uri) throws IOException {
		try {
			URL url = uri.toURL();
			HttpURLConnection htc = (HttpURLConnection) url.openConnection();
			return new HttpConnectionWrapper(htc);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

}
