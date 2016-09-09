package com.boluozhai.snowflake.rest.impl.client;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rest.client.RestAPI;
import com.boluozhai.snowflake.rest.client.RestService;

public class ImpRestService implements RestService {

	private final SnowflakeContext _context;
	private final URI _uri;

	public ImpRestService(ImpRestClient client, URI uri) {
		this._context = client.getContext();
		this._uri = uri;
	}

	@Override
	public URI getURI() {
		return this._uri;
	}

	@Override
	public String getName() {
		return this._uri.getPath();
	}

	@Override
	public RestAPI getAPI(String name) {
		URI uri = RestUriTools.child(this._uri, name);
		return new ImpRestAPI(this, uri, name);
	}

	@Override
	public SnowflakeContext getContext() {
		return this._context;
	}

}
