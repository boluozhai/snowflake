package com.boluozhai.snowflake.xgit.http.impl;

import java.net.URI;

import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.http.HttpRepository;
import com.boluozhai.snowflake.xgit.http.base.HttpXGitComponentBuilder;

public class XHttpRepoImpl extends HttpRepository {

	public static HttpXGitComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder extends HttpXGitComponentBuilder {

		@Override
		public Component create(ComponentContext cc) {
			XGitContext gc = (XGitContext) cc;
			return new XHttpRepoImpl(gc);
		}

	}

	private class Life implements ComponentLifecycle {

		@Override
		public void onCreate() {

		}

	}

	private final XGitContext _context;

	private XHttpRepoImpl(XGitContext cc) {
		this._context = cc;
	}

	@Override
	public XGitContext context() {
		return this._context;
	}

	@Override
	public URI location() {
		return this._context.getURI();
	}

	@Override
	public ComponentLifecycle lifecycle() {
		return new Life();
	}

	@Override
	public ComponentContext getComponentContext() {
		return this._context;
	}

}
