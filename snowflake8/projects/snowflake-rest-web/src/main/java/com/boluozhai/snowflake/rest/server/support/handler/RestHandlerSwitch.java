package com.boluozhai.snowflake.rest.server.support.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.rest.server.RestRequestHandler;

public class RestHandlerSwitch implements RestRequestHandler {

	private RestHandlerSwitchExpression expression;
	private String pathPartName; // a simple way to use the 'expression'
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

	public String getPathPartName() {
		return pathPartName;
	}

	public void setPathPartName(String pathPartName) {
		this.pathPartName = pathPartName;
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

		RestHandlerSwitchExpression exp = this.inner_get_expression();
		String key = exp.getValue(request);
		RestRequestHandler next = this.handlers.get(key);
		if (next == null) {
			next = this.defaultHandler;
		}
		next.handle(request, response);

	}

	private RestHandlerSwitchExpression inner_get_expression() {
		RestHandlerSwitchExpression exp = this.expression;
		if (exp == null) {
			String pp_name = this.pathPartName;
			if (pp_name == null) {
				String msg = "both of the [expression] & [pathPartName] is null.";
				throw new SnowflakeException(msg);
			}
			exp = new PathNameExpression(pp_name);
			this.expression = exp;
		}
		return exp;
	}

}