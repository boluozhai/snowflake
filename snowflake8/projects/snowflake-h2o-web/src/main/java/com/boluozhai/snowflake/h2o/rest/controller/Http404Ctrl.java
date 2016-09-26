package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestController;

public class Http404Ctrl extends RestController {

	private void make_http_404() throws ServletException {
		throw new ServletException("http 404 not found");
	}

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		this.make_http_404();

	}

	@Override
	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		this.make_http_404();
	}

	@Override
	protected void rest_put(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		this.make_http_404();
	}

	@Override
	protected void rest_delete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		this.make_http_404();
	}

}
