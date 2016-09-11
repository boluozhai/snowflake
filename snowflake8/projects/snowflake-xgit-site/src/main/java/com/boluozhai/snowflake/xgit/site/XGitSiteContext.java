package com.boluozhai.snowflake.xgit.site;

import com.boluozhai.snowflake.xgit.XGitContext;

public interface XGitSiteContext extends XGitContext {

	interface SITE_URI {

		String system = "site:/system";
		String data = "site:/data/";
		String user = "site:/user/";

	}

	interface component {

		String repositories = "repositories";
		String users = "users";

		String system_repo = "system_repo";
		String user_repo = "user_repo";
		String data_repo = "data_repo";

	}

}
