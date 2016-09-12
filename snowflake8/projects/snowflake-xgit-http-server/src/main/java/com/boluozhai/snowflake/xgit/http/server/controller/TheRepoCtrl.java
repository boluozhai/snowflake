package com.boluozhai.snowflake.xgit.http.server.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.RestServlet.RestInfo;
import com.boluozhai.snowflake.xgit.http.server.XGitHttpInfo;
import com.boluozhai.snowflake.xgit.http.server.XGitRestInfoWrapper;
import com.google.gson.Gson;

public class TheRepoCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		RestInfo info_0 = this.getRestInfo(request);
		XGitHttpInfo info = new XGitRestInfoWrapper(info_0);

		String repo_name = info.getRepoName();

		DataModel dm = new DataModel();
		dm.repository = repo_name;
		dm.service = "move to page URL";

		Gson gs = new Gson();
		String s = gs.toJson(dm);
		response.getOutputStream().print(s);

	}

	public static class DataModel {

		private String service;
		private String repository;

		public String getService() {
			return service;
		}

		public void setService(String service) {
			this.service = service;
		}

		public String getRepository() {
			return repository;
		}

		public void setRepository(String repository) {
			this.repository = repository;
		}

	}

}
