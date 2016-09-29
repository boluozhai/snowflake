package com.boluozhai.snowflake.xgit.remotes;

import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface RemoteManager {

	class Factory {

		public static RemoteManager getInstance(Repository repo) {
			ComponentContext cc = repo.getComponentContext();
			String key = XGitContext.component.remotes;
			return cc.getBean(key, RemoteManager.class);
		}

	}

	void put(String name, Remote remote);

	Remote get(String name);

	/***
	 * @return the name list
	 * */

	String[] list();

}
