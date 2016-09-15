package com.boluozhai.snowflake.xgit.refs;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitComponent;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface HrefManager extends XGitComponent {

	interface name {

		// refnames

		String HEAD = "HEAD";
		String SECTION_HEAD = "SECTION_HEAD";

		// refs

		String refs = "refs";

		String refs_tags = "refs/tags";
		String refs_heads = "refs/heads";
		String refs_remotes = "refs/remotes";

		String refs_xgit = "refs/xgit";

	}

	class Factory {

		public static HrefManager getInstance(Repository repo) {
			XGitContext context = repo.context();
			String key = XGitContext.component.hrefs;
			return context.getBean(key, HrefManager.class);
		}

	}

	/***
	 * this method will cause a finding-process
	 * */

	Ref findRef(String name);

	/***
	 * this method will cause a finding-process
	 * */

	ObjectId findId(String name);

}
