package com.boluozhai.snowflake.xgit.http.server.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestController;

public class TheNullCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getOutputStream().print("Forbidden");

	}

}
