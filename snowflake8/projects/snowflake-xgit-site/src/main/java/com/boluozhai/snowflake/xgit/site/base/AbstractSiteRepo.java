package com.boluozhai.snowflake.xgit.site.base;

import java.net.URI;

import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.site.SiteRepository;
import com.boluozhai.snowflake.xgit.workspace.Workspace;

public class AbstractSiteRepo implements SiteRepository {

	protected final XGitContext context;

	public AbstractSiteRepo(XGitContext context) {
		this.context = context;
	}

	@Override
	public XGitContext context() {
		return context;
	}

	@Override
	public URI location() {
		return context.getURI();
	}

	@Override
	public ComponentLifecycle lifecycle() {
		return new Life();
	}

	@Override
	public ComponentContext getComponentContext() {
		return context;
	}

	protected class Life implements ComponentLifecycle {

		@Override
		public void onCreate() {
		}
	}

	@Override
	public Workspace getWorking() {
		return null;
	}

}
