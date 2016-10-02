package com.boluozhai.snowflake.xgit.http.server.listener;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.path.PathPart;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.rest.server.support.handler.RestRequestListener;
import com.boluozhai.snowflake.xgit.http.server.GitHttpInfo;

public class RepoInfoBindingListener implements RestRequestListener {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// TODO Auto-generated method stub

		RestRequestInfo rest_info = RestRequestInfo.Factory
				.getInstance(request);
		PathInfo path_info = rest_info.getPathInfo();

		RepoUrlMaker url_mk = new RepoUrlMaker();
		url_mk.setRawURL(request.getRequestURL().toString());
		url_mk.setPathParts(path_info);

		String user = path_info.getRequiredPart("user").toString();
		String repo = path_info.getRequiredPart("repository").toString();
		String type = path_info.getRequiredPart("type").toString();
		String id = path_info.getRequiredPart("id").toString();

		GitHttpInfo info = new GitHttpInfo();
		info.url = url_mk.create();
		info.user = user;
		info.repository = repo;
		info.resource = type + "/" + id;
		info.service = request.getParameter("service");

		GitHttpInfo.Agent.put(info, request);

		info = GitHttpInfo.Agent.get(request);

	}

	private static class RepoUrlMaker {

		private String _raw;
		private String _keyword;

		public void setRawURL(String string) {
			this._raw = string;
		}

		public void setPathParts(PathInfo path_info) {

			// PathPart full = path_info.getFullPart();
			PathPart rep = path_info.getRequiredPart("repository");
			this._keyword = rep.toString();

		}

		public String create() {
			String url = this._raw;
			String key = this._keyword;

			int i = url.lastIndexOf(key);
			if (i < 0) {
				return null;
			}
			return url.substring(0, i + key.length());

		}

	}

}
