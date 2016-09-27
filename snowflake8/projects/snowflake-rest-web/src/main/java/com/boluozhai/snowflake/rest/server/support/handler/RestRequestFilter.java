package com.boluozhai.snowflake.rest.server.support.handler;

import com.boluozhai.snowflake.rest.server.RestRequestHandler;

public abstract class RestRequestFilter implements RestRequestHandler {

	private RestRequestHandler nextHanlder;
	private RestRequestListener[] listeners;

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

}
