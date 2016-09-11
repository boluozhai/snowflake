package com.boluozhai.snowflake.xgit.site.base;

import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.site.SiteRepositoryManager;
import com.boluozhai.snowflake.xgit.site.SiteUserManager;
import com.boluozhai.snowflake.xgit.site.SystemRepository;

public class AbstractSystemRepo extends AbstractSiteRepo implements
		SystemRepository {

	public AbstractSystemRepo(XGitContext context) {
		super(context);
	}

	@Override
	public SiteUserManager getUserManager() {
		throw new RuntimeException("no impl");
	}

	@Override
	public SiteRepositoryManager getRepositoryManager() {
		throw new RuntimeException("no impl");
	}

}
