package com.boluozhai.snowflake.rest.impl.client;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rest.client.RESTClient;
import com.boluozhai.snowflake.rest.client.RestService;

final class ImpRestClient implements RESTClient {

	private final SnowflakeContext _context;

	public ImpRestClient(SnowflakeContext context) {
		this._context = context;
	}

	@Override
	public RestService getService(URI uri) {
		return new ImpRestService(this, uri);
	}

	@Override
	public SnowflakeContext getContext() {
		return this._context;
	}

}
