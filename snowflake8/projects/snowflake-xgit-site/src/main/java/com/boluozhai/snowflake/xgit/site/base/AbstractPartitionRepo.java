package com.boluozhai.snowflake.xgit.site.base;

import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.site.PartitionRepository;

public class AbstractPartitionRepo extends AbstractSiteRepo implements
		PartitionRepository {

	public AbstractPartitionRepo(XGitContext context) {
		super(context);
	}

}
