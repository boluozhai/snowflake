package com.boluozhai.snowflake.rest.impl.client;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rest.client.RestAPI;
import com.boluozhai.snowflake.rest.client.RestService;
import com.boluozhai.snowflake.rest.client.RestType;

public class ImpRestAPI implements RestAPI {

	private final SnowflakeContext _context;
	private final RestService _owner;
	private final String _name;
	private final URI _uri;

	public ImpRestAPI(ImpRestService app, URI uri, String name) {
		this._context = app.getContext();
		this._owner = app;
		this._name = name;
		this._uri = uri;
	}

	@Override
	public SnowflakeContext getContext() {
		return this._context;
	}

	@Override
	public URI getURI() {
		return this._uri;
	}

	@Override
	public String getName() {
		return this._name;
	}

	@Override
	public RestService getOwner() {
		return this._owner;
	}

	@Override
	public RestType getType(String name) {
		URI uri = RestUriTools.child(_uri, name);
		return new ImpRestType(this, uri, name);
	}

}
