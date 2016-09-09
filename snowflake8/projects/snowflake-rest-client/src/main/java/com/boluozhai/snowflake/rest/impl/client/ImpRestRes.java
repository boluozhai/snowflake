package com.boluozhai.snowflake.rest.impl.client;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rest.client.RestRequest;
import com.boluozhai.snowflake.rest.client.RestResource;
import com.boluozhai.snowflake.rest.client.RestType;

public class ImpRestRes implements RestResource {

	private final URI _uri;
	private final String _id;
	private final RestType _owner;
	private final SnowflakeContext _context;

	public ImpRestRes(ImpRestType type, URI uri, String id) {
		this._context = type.getContext();
		this._owner = type;
		this._uri = uri;
		this._id = id;
	}

	@Override
	public SnowflakeContext getContext() {
		return this._context;
	}

	@Override
	public RestType getOwner() {
		return this._owner;
	}

	@Override
	public URI getURI() {
		return this._uri;
	}

	@Override
	public String getId() {
		return this._id;
	}

	@Override
	public RestRequest get() {
		return new ImpRestRequest(this, "GET");
	}

	@Override
	public RestRequest post() {
		return new ImpRestRequest(this, "POST");
	}

	@Override
	public RestRequest put() {
		return new ImpRestRequest(this, "PUT");
	}

	@Override
	public RestRequest delete() {
		return new ImpRestRequest(this, "DELETE");
	}

}
