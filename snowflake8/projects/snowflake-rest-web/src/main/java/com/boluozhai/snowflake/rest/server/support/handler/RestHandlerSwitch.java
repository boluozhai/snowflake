package com.boluozhai.snowflake.rest.server.support.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RequestHandler;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathPart;

public class RestHandlerSwitch implements RequestHandler {

	private String pathPartName;
	private Map<String, RequestHandler> handlers;
	private RequestHandler defaultHandler;

	public RequestHandler getDefaultHandler() {
		return defaultHandler;
	}

	public void setDefaultHandler(RequestHandler defaultHandler) {
		this.defaultHandler = defaultHandler;
	}

	public String getPathPartName() {
		return pathPartName;
	}

	public void setPathPartName(String pathPartName) {
		this.pathPartName = pathPartName;
	}

	public Map<String, RequestHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(Map<String, RequestHandler> handlers) {
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
		RequestHandler next = this.handlers.get(key);
		if (next == null) {
			next = this.defaultHandler;
		}

		next.handle(request, response);

	}

}
