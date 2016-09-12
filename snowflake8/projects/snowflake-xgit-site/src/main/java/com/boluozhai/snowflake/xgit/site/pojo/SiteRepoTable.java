package com.boluozhai.snowflake.xgit.site.pojo;

import java.util.Map;

public class SiteRepoTable extends SiteDoc {

	private Map<String, SiteRepoInfo> repositories;

	public Map<String, SiteRepoInfo> getRepositories() {
		return repositories;
	}

	public void setRepositories(Map<String, SiteRepoInfo> repositories) {
		this.repositories = repositories;
	}

}
