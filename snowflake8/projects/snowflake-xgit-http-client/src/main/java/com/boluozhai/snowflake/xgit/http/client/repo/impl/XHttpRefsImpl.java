package com.boluozhai.snowflake.xgit.http.client.repo.impl;

import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.xgit.http.client.base.HttpXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.http.client.repo.HttpRefs;
import com.boluozhai.snowflake.xgit.refs.Ref;

public class XHttpRefsImpl extends HttpRefs {

	public static HttpXGitComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder extends HttpXGitComponentBuilder {
		@Override
		public Component create(ComponentContext cc) {
			return new XHttpRefsImpl(cc);
		}
	}

	private class Life implements ComponentLifecycle {

		@Override
		public void onCreate() {

		}

	}

	private final ComponentContext _context;

	private XHttpRefsImpl(ComponentContext cc) {
		this._context = cc;
	}

	@Override
	public Ref getReference(String name) {
		// TODO Auto-generated method stub
		return null;
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
	public String[] list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] list(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

}
