package com.boluozhai.snowflake.rest.server.info.path;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.api.h2o.PathModel;

public class PathInfoHolder {

	private final static String key = PathInfoHolder.class.getName()
			+ ".binding";

	private final HttpServletRequest request;

	private PathInfoHolder(HttpServletRequest aRequest) {
		this.request = aRequest;
	}

	public static PathInfoHolder create(HttpServletRequest aRequest) {
		return new PathInfoHolder(aRequest);
	}

	public PathModel get() {
		PathModel model = (PathModel) request.getAttribute(key);
		if (model == null) {
			PathModelBuilder builder = new PathModelBuilder(this);
			model = builder.create();
			this.set(model);
		}
		return model;
	}

	public void set(PathModel model) {
		request.setAttribute(key, model);
	}

	private class PathModelBuilder {

		public PathModelBuilder(PathInfoHolder pathInfoHolder) {
			// TODO Auto-generated constructor stub
		}

		public PathModel create() {
			// TODO Auto-generated method stub
			throw new RuntimeException("no impl");
			// return null;
		}
	}

}
