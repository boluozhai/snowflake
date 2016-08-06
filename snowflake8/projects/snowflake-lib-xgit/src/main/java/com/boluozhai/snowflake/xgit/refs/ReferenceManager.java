package com.boluozhai.snowflake.xgit.refs;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitComponent;

public interface ReferenceManager extends XGitComponent {

	interface name {

		String HEAD = "HEAD";

		String refs = "refs";

		String refs_tags = "refs/tags";
		String refs_heads = "refs/heads";
		String refs_remotes = "refs/remotes";

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
