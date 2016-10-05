package com.boluozhai.snowflake.rest.server.support.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestRequestHandler;

public class RestHandlerSwitch implements RestRequestHandler {

	private RestHandlerSwitchExpression expression;
	private Map<String, RestRequestHandler> handlers;
	private RestRequestHandler defaultHandler;

	public RestHandlerSwitchExpression getExpression() {
		return expression;
	}

	public void setExpression(RestHandlerSwitchExpression expression) {
		this.expression = expression;
	}

	public RestRequestHandler getDefaultHandler() {
		return defaultHandler;
	}

	public void setDefaultHandler(RestRequestHandler defaultHandler) {
		this.defaultHandler = defaultHandler;
	}

	public Map<String, RestRequestHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(Map<String, RestRequestHandler> handlers) {
		this.handlers = handlers;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RestHandlerSwitchExpression exp = this.expression;
		final String key = exp.getValue(request);
		RestRequestHandler next = this.handlers.get(key);
		if (next == null) {
			next = this.defaultHandler;
		}

		try {
			next.handle(request, response);
		} catch (Exception e) {

			String msg = "rest_switch(key=%s)";
			msg = String.format(msg, key);
			throw new ServletException(msg, e);

		}

	}
}
