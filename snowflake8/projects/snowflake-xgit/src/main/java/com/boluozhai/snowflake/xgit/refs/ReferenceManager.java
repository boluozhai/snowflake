package com.boluozhai.snowflake.xgit.refs;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitComponent;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface ReferenceManager extends XGitComponent {

	interface name {

		String HEAD = "HEAD";
		String SECTION_HEAD = "SECTION_HEAD";

		String refs = "refs";

		String refs_tags = "refs/tags";
		String refs_heads = "refs/heads";
		String refs_remotes = "refs/remotes";

		String refs_xgit = "refs/xgit";

	}

	class Factory {

		public static ReferenceManager getInstance(Repository repo) {
			XGitContext context = repo.context();
			String key = XGitContext.component.refs;
			return context.getBean(key, ReferenceManager.class);
		}

	}

	Reference getReference(String name);

	/***
	 * this method will cause a finding-process
	 * */

	Reference findTargetReference(String name);

	/***
	 * this method will cause a finding-process
	 * */

	ObjectId findTargetId(String name);

}
