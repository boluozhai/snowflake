package com.boluozhai.snowflake.rest.impl.client;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rest.client.RestAPI;
import com.boluozhai.snowflake.rest.client.RestResource;
import com.boluozhai.snowflake.rest.client.RestType;

public class ImpRestType implements RestType {

	private final SnowflakeContext _context;
	private final RestAPI _owner;
	private final String _name;
	private final URI _uri;

	public ImpRestType(ImpRestAPI api, URI uri, String name) {
		this._context = api.getContext();
		this._owner = api;
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
	public RestAPI getOwner() {
		return this._owner;
	}

	@Override
	public String getName() {
		return this._name;
	}

	@Override
	public RestResource getResource(String id) {
		URI uri = RestUriTools.child(_uri, id, false);
		return new ImpRestRes(this, uri, id);
	}

}
