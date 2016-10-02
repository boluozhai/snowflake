package com.boluozhai.snowflake.rest.server.support.handler;

import com.boluozhai.snowflake.rest.server.RestRequestHandler;

public abstract class AbstractRequestHandler implements RestRequestHandler {

	private RestRequestListener[] listeners;
	private RestRequestFilter[] filters;
	private RestRequestHandler nextHanlder;

	public RestRequestHandler getNextHanlder() {
		return nextHanlder;
	}

	public void setNextHanlder(RestRequestHandler nextHanlder) {
		this.nextHanlder = nextHanlder;
	}

	public RestRequestListener[] getListeners() {
		return listeners;
	}

	public void setListeners(RestRequestListener[] listeners) {
		this.listeners = listeners;
	}

	public RestRequestFilter[] getFilters() {
		return filters;
	}

	public void setFilters(RestRequestFilter[] filters) {
		this.filters = filters;
	}

}
