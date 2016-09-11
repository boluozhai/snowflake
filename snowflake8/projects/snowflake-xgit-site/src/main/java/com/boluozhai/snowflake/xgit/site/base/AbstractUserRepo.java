package com.boluozhai.snowflake.xgit.site.base;

import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.site.UserRepository;

public class AbstractUserRepo extends AbstractSiteRepo implements
		UserRepository {

	public AbstractUserRepo(XGitContext context) {
		super(context);
	}

}
