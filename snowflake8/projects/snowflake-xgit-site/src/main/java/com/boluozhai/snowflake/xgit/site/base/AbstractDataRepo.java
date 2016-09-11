package com.boluozhai.snowflake.xgit.site.base;

import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.site.DataRepository;

public class AbstractDataRepo extends AbstractSiteRepo implements
		DataRepository {

	public AbstractDataRepo(XGitContext context) {
		super(context);
	}

}
