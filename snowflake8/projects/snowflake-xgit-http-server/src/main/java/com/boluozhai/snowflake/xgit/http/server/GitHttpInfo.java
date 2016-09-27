package com.boluozhai.snowflake.xgit.http.server;

import javax.servlet.ServletRequest;

public class GitHttpInfo {

	/***
	 * the repository URL
	 * */

	public String url;

	/***
	 * the repository name
	 * */

	public String repository;

	/***
	 * the user name
	 * */

	public String user;

	/***
	 * the service name
	 * */

	public String service;

	/***
	 * the resource path
	 * */

	public String resource;

	public static class Agent {

		private static final String key = GitHttpInfo.class.getName()
				+ ".binding";

		public static GitHttpInfo get(ServletRequest request) {
			GitHttpInfo info = (GitHttpInfo) request.getAttribute(key);
			if (info == null) {
				String msg = "no attr[%s] at the http-request.";
				msg = String.format(msg, key);
				throw new RuntimeException(msg);
			}
			return info;
		}

		public static void put(GitHttpInfo info, ServletRequest request) {
			request.setAttribute(key, info);
		}

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
