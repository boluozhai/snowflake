package com.boluozhai.snowflake.xgit.http.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestController;

public class GitHttpController extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		throw new ServletException("no impl");

	}

	@Override
	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		throw new ServletException("no impl");

	}

	@Override
	protected void rest_put(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		throw new ServletException("no impl");

	}

	@Override
	protected void rest_delete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		throw new ServletException("no impl");

	}

}
