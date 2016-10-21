package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestController;

public class FileCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		WorkingDirCtrl next = new WorkingDirCtrl();
		next.rest_get(request, response);
	}

	@Override
	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		WorkingDirCtrl next = new WorkingDirCtrl();
		next.rest_post(request, response);
	}

	@Override
	protected void rest_put(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		WorkingDirCtrl next = new WorkingDirCtrl();
		next.rest_put(request, response);
	}

	@Override
	protected void rest_delete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		WorkingDirCtrl next = new WorkingDirCtrl();
		next.rest_delete(request, response);
	}

}
