package com.boluozhai.snowflake.xgit.http.impl;

import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.xgit.http.AbstractHttpGitClient;
import com.boluozhai.snowflake.xgit.http.base.HttpXGitComponentBuilder;

public class XHttpGitClientImpl extends AbstractHttpGitClient {

	public static HttpXGitComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder extends HttpXGitComponentBuilder {

		@Override
		public Component create(ComponentContext cc) {
			return new XHttpGitClientImpl(cc);
		}

	}

	private class Life implements ComponentLifecycle {

		@Override
		public void onCreate() {
		}

	}

	private final ComponentContext _context;

	private XHttpGitClientImpl(ComponentContext cc) {
		this._context = cc;
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
