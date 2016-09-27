package com.boluozhai.snowflake.rest.server.support.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.path.PathPart;
import com.boluozhai.snowflake.rest.server.RestRequestHandler;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;

public class RestHandlerSwitch implements RestRequestHandler {

	private String pathPartName;
	private Map<String, RestRequestHandler> handlers;
	private RestRequestHandler defaultHandler;

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

		// make key name
		RestRequestInfo info = RestRequestInfo.Factory.getInstance(request);
		PathInfo path_info = info.getPathInfo();
		PathPart pp = path_info.getPart(this.pathPartName, true);
		String key = pp.toString();

		// find next handler
		RestRequestHandler next = this.handlers.get(key);
		if (next == null) {
			next = this.defaultHandler;
		}

		next.handle(request, response);

	}

}
