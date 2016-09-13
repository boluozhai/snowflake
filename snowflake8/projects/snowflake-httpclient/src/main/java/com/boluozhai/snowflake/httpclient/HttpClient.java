package com.boluozhai.snowflake.httpclient;

import java.io.IOException;
import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface HttpClient {

	class Factory {

		public static HttpClient getInstance(SnowflakeContext context) {
			Class<HttpClientFactory> type = HttpClientFactory.class;
			String key = type.getName();
			HttpClientFactory factory = context.getBean(key, type);
			return factory.getClient(context);
		}

	}

	HttpConnection open(URI uri) throws IOException;

}
