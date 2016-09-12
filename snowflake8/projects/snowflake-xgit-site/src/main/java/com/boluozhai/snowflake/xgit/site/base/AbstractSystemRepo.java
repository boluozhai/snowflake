package com.boluozhai.snowflake.xgit.site.base;

import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.site.SiteRepositoryManager;
import com.boluozhai.snowflake.xgit.site.SiteUserManager;
import com.boluozhai.snowflake.xgit.site.SystemRepository;
import com.boluozhai.snowflake.xgit.site.XGitSiteContext;

public class AbstractSystemRepo extends AbstractSiteRepo implements
		SystemRepository {

	private SiteUserManager _user_man;
	private SiteRepositoryManager _repo_man;

	public AbstractSystemRepo(XGitContext context) {
		super(context);
	}

	@Override
	public SiteUserManager getUserManager() {
		SiteUserManager um = this._user_man;
		if (um == null) {
			XGitContext cc = this.context;
			String key = XGitSiteContext.component.users;
			um = cc.getBean(key, SiteUserManager.class);
			this._user_man = um;
		}
		return um;
	}

	@Override
	public SiteRepositoryManager getRepositoryManager() {
		SiteRepositoryManager rm = this._repo_man;
		if (rm == null) {
			XGitContext cc = this.context;
			String key = XGitSiteContext.component.repositories;
			rm = cc.getBean(key, SiteRepositoryManager.class);
			this._repo_man = rm;
		}
		return rm;
	}

}
