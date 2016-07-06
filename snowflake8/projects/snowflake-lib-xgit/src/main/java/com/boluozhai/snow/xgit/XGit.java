package com.boluozhai.snow.xgit;

import com.boluozhai.snow.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.context.SnowContext;

public class XGit {

	public static RepositoryManager getRepositoryManager(SnowContext context) {
		String key = RepositoryManager.class.getName();
		return context.getBean(key, RepositoryManager.class);
	}

}
