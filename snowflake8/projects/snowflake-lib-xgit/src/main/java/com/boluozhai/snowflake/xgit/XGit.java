package com.boluozhai.snowflake.xgit;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;

public class XGit {

	public static RepositoryManager getRepositoryManager(SnowContext context) {
		String key = RepositoryManager.class.getName();
		return context.getBean(key, RepositoryManager.class);
	}

}
