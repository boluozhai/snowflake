package com.boluozhai.snowflake.xgit.site.impl;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.site.MimeTypeRegistrar;
import com.boluozhai.snowflake.xgit.site.RepositorySpaceAllocator;
import com.boluozhai.snowflake.xgit.site.SystemRepository;
import com.boluozhai.snowflake.xgit.site.XGitSite;
import com.boluozhai.snowflake.xgit.site.XGitSiteContext;

final class XGitSiteImpl implements XGitSite {

	private final SnowflakeContext _context;
	private SystemRepository _sys_repo;
	private MimeTypeRegistrar _mime_type_reg;

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

		String url = "site-system:///";
		SnowflakeContext context = this._context;
		RepositoryManager rm = XGit.getRepositoryManager(context);
		Repository repo = rm.open(context, URI.create(url), null);

		XGitContext cc = repo.context();
		String key = XGitSiteContext.component.system_repo;
		return cc.getBean(key, SystemRepository.class);

	}

	@Override
	public RepositorySpaceAllocator getRepositorySpaceAllocator() {
		return new XGitSiteRepoAllocatorImpl(this);
	}

	@Override
	public MimeTypeRegistrar getMimeTypeRegistrar() {
		MimeTypeRegistrar reg = this._mime_type_reg;
		if (reg == null) {
			SystemRepository sys = this.getSystemRepository();
			ComponentContext cc = sys.getComponentContext();
			String key = XGitContext.component.mime_types;
			reg = (MimeTypeRegistrar) cc.getBean(key);
			this._mime_type_reg = reg;
		}
		return reg.cache();
	}

}
