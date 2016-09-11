package com.boluozhai.snowflake.xgit.site;

import com.boluozhai.snowflake.xgit.repository.Repository;

public interface SiteRepository extends Repository {

	interface TYPE {

		String system = "system";
		String data = "data";
		String user = "user";

	}

}
