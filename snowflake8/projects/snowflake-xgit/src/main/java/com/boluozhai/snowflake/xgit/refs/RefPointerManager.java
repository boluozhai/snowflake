package com.boluozhai.snowflake.xgit.refs;

import com.boluozhai.snowflake.xgit.XGitComponent;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface RefPointerManager extends XGitComponent {

	class Factory {

		public static RefPointerManager getInstance(Repository repo) {
			XGitContext context = repo.context();
			String key = XGitContext.component.refptrs;
			return context.getBean(key, RefPointerManager.class);
		}

	}

	RefPointer getPointer(String name);

}
