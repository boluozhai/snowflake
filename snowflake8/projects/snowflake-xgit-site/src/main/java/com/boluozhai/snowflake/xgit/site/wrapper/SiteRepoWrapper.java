package com.boluozhai.snowflake.xgit.site.wrapper;

import java.net.URI;

import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.site.SiteRepository;
import com.boluozhai.snowflake.xgit.workspace.Workspace;

public class SiteRepoWrapper implements SiteRepository {

	protected final SiteRepository inner;

	public SiteRepoWrapper(SiteRepository in) {
		this.inner = in;
	}

	public ComponentContext getComponentContext() {
		return inner.getComponentContext();
	}

	public ComponentLifecycle lifecycle() {
		return inner.lifecycle();
	}

	public XGitContext context() {
		return inner.context();
	}

	public URI location() {
		return inner.location();
	}

	@Override
	public Workspace getWorking() {
		return inner.getWorking();
	}

}
