package com.boluozhai.snowflake.xgit.refs;

import com.boluozhai.snowflake.xgit.XGitComponent;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface RefManager extends XGitComponent {

	class Factory {

		public static RefManager getInstance(Repository repo) {
			XGitContext context = repo.context();
			String key = XGitContext.component.refs;
			return context.getBean(key, RefManager.class);
		}

	}

	interface prefix {

		String refs = "refs/";
		String xgit_private_refs = "xgit.private_refs/";

	}

	Ref getReference(String name);

	/***
	 * the name list of all refs
	 * */

	String[] list();

	String[] list(String prefix);

}
