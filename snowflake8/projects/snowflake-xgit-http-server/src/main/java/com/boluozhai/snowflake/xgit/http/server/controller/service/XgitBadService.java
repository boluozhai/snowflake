package com.boluozhai.snowflake.xgit.http.server.controller.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestController;

public class XgitBadService extends RestController {

	private void respond(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// HTTP 403 Forbidden

		String service = request.getParameter("service");
		String msg = "HTTP 403 Forbidden (service=%s)";
		msg = String.format(msg, service);

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getOutputStream().print(msg);

	}

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.respond(request, response);
	}

	@Override
	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.respond(request, response);
	}

	@Override
	protected void rest_put(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.respond(request, response);
	}

	@Override
	protected void rest_delete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.respond(request, response);
	}

}
