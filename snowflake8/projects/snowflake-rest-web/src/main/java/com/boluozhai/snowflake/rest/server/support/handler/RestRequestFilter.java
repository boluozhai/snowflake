package com.boluozhai.snowflake.rest.server.support.handler;

import com.boluozhai.snowflake.rest.server.RequestHandler;

public abstract class RestRequestFilter implements RequestHandler {

	private RequestHandler nextHanlder;
	private RestRequestListener[] listeners;

	public RequestHandler getNextHanlder() {
		return nextHanlder;
	}

	public void setNextHanlder(RequestHandler nextHanlder) {
		this.nextHanlder = nextHanlder;
	}

	public RestRequestListener[] getListeners() {
		return listeners;
	}

	public void setListeners(RestRequestListener[] listeners) {
		this.listeners = listeners;
	}

}
