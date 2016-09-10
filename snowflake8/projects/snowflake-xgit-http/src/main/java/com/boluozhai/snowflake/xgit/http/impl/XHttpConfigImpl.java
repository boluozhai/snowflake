package com.boluozhai.snowflake.xgit.http.impl;

import java.io.IOException;
import java.net.URI;

import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.xgit.http.HttpConfig;
import com.boluozhai.snowflake.xgit.http.base.HttpXGitComponentBuilder;

public class XHttpConfigImpl extends HttpConfig {

	public static HttpXGitComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder extends HttpXGitComponentBuilder {
		@Override
		public Component create(ComponentContext cc) {
			return new XHttpConfigImpl(cc);
		}
	}

	private class Life implements ComponentLifecycle {

		@Override
		public void onCreate() {

		}

	}

	private final ComponentContext _context;

	private XHttpConfigImpl(ComponentContext cc) {
		this._context = cc;
	}

	@Override
	public void load() throws IOException {
		// TODO Auto-generated method stub

		URI uri = this._context.getURI();
		System.out.println("load http-repo-config from " + uri);

	}

	@Override
	public void save() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public ComponentLifecycle lifecycle() {
		return new Life();
	}

	@Override
	public ComponentContext getComponentContext() {
		return this._context;
	}

	@Override
	public void setProperty(String key, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProperty(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProperty(String name, Object defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

}
