package com.boluozhai.snowflake.rest.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;

public class RestController implements RestRequestHandler {

	@Override
	public final void handle(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String method = request.getMethod();

		if (method == null) {

		} else if (method.equalsIgnoreCase("GET")) {
			this.rest_get(request, response);

		} else if (method.equalsIgnoreCase("PUT")) {
			this.rest_put(request, response);

		} else if (method.equalsIgnoreCase("POST")) {
			this.rest_post(request, response);

		} else if (method.equalsIgnoreCase("DELETE")) {
			this.rest_delete(request, response);

		} else {
			// NOP
		}

	}

	public final RestRequestInfo getRestInfo(HttpServletRequest request) {
		return RestRequestInfo.Factory.getInstance(request);
	}

	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void rest_put(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void rest_delete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
