package com.boluozhai.snowflake.xgit.site;

import com.boluozhai.snowflake.xgit.config.Config;

public interface SiteRepositoryConfig extends Config {

	interface site {

		String repo_type = xgit.siterepositorytype;
		String repo_descriptor = xgit.siterepositorydescriptor;

	}

}
