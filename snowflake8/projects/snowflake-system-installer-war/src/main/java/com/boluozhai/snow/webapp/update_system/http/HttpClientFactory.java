package com.boluozhai.snow.webapp.update_system.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientFactory {

	public HttpClient getClient() {

		return new DefaultHttpClient();

	}

}
