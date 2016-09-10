package com.boluozhai.snowflake.xgit.site.impl;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.site.SystemRepository;
import com.boluozhai.snowflake.xgit.site.XGitSite;

final class XGitSiteImpl implements XGitSite {

	private final SnowflakeContext _context;
	private SystemRepository _sys_repo;

	public XGitSiteImpl(SnowflakeContext context) {
		this._context = context;
	}

	@Override
	public SystemRepository getSystemRepository() {
		SystemRepository sys_repo = this._sys_repo;
		if (sys_repo == null) {
			sys_repo = this.inner_load_system_repo();
			this._sys_repo = sys_repo;
		}
		return sys_repo;
	}

	private SystemRepository inner_load_system_repo() {
		String url = "site:/system";
		SnowflakeContext context = this._context;
		RepositoryManager rm = XGit.getRepositoryManager(context);
		Repository repo = rm.open(context, URI.create(url), null);
		XGitContext cc = repo.context();
		return cc.getBean("system_repository", SystemRepository.class);
	}

}
