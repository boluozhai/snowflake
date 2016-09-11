package com.boluozhai.snowflake.xgit.site.wrapper;

import com.boluozhai.snowflake.xgit.site.SiteRepositoryManager;
import com.boluozhai.snowflake.xgit.site.SiteUserManager;
import com.boluozhai.snowflake.xgit.site.SystemRepository;

public class SystemRepoWrapper extends SiteRepoWrapper implements
		SystemRepository {

	protected final SystemRepository inner_system;

	public SystemRepoWrapper(SystemRepository in) {
		super(in);
		this.inner_system = in;
	}

	@Override
	public SiteUserManager getUserManager() {
		return inner_system.getUserManager();
	}

	@Override
	public SiteRepositoryManager getRepositoryManager() {
		return inner_system.getRepositoryManager();
	}

}
