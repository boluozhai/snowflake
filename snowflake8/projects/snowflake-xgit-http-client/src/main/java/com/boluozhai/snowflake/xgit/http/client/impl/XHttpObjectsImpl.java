package com.boluozhai.snowflake.xgit.http.client.impl;

import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.http.client.HttpObjects;
import com.boluozhai.snowflake.xgit.http.client.base.HttpXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;

public class XHttpObjectsImpl extends HttpObjects {

	public static HttpXGitComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder extends HttpXGitComponentBuilder {
		@Override
		public Component create(ComponentContext cc) {
			return new XHttpObjectsImpl(cc);
		}
	}

	private class Life implements ComponentLifecycle {

		@Override
		public void onCreate() {

		}

	}

	private final ComponentContext _context;

	private XHttpObjectsImpl(ComponentContext cc) {
		this._context = cc;
	}

	@Override
	public GitObject object(ObjectId id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GitObjectBuilder newBuilder(String type, long length) {
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
}
