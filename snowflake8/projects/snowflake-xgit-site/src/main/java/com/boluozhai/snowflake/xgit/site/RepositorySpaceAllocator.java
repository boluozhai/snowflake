package com.boluozhai.snowflake.xgit.site;

import java.net.URI;
import java.util.Map;

public interface RepositorySpaceAllocator {

	interface PARAM {

		String repo_type = "repo-type";
		String repo_name = "repo-name";
		String owner_uid = "owner-uid";

	}

	interface TYPE extends RepositoryType {

		// [SYSTEM|PARTITION|DATA|USER|NORMAL]

	}

	/***************
	 * @param param
	 *            parameters for the new repository.
	 * @return the location (file:///) of the new repository.
	 * */

	URI allocate(Map<String, String> param);

}
