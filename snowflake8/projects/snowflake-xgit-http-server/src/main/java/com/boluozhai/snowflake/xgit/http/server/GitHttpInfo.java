package com.boluozhai.snowflake.xgit.http.server;

import javax.servlet.ServletRequest;

public class GitHttpInfo {

	public String url; // the repo URL
	public String name; // the repo name
	public String service;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

}
