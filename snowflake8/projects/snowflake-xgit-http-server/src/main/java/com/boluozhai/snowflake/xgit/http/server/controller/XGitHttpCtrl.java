package com.boluozhai.snowflake.xgit.http.server.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.RestServlet.RestInfo;
import com.boluozhai.snowflake.xgit.http.server.XGitHttpInfo;
import com.boluozhai.snowflake.xgit.http.server.XGitRestInfoWrapper;

public class XGitHttpCtrl extends RestController {

	private RestController _ctrl_null;
	private RestController _ctrl_repo;
	private RestController _ctrl_repo_info_refs;

	public XGitHttpCtrl() {

		this._ctrl_null = new TheNullCtrl();
		this._ctrl_repo = new TheRepoCtrl();
		this._ctrl_repo_info_refs = new TheInfoRefsCtrl();

	}

	protected void rest_all(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		RestInfo info1 = this.getRestInfo(request);
		XGitHttpInfo info2 = new XGitRestInfoWrapper(info1);
		String path = info2.getPathInRepo();
		RestController next = null;
		if (path == null) {
			next = this._ctrl_null;
		} else if (path.equals("")) {
			next = this._ctrl_repo;
		} else if (path.equals("info/refs")) {
			next = this._ctrl_repo_info_refs;
		} else {
			next = this._ctrl_null;
		}

		next.forward(request, response);

	}

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.rest_all(request, response);
	}

	@Override
	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.rest_all(request, response);
	}

	@Override
	protected void rest_put(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.rest_all(request, response);
	}

	@Override
	protected void rest_delete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.rest_all(request, response);
	}

}
