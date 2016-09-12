package com.boluozhai.snowflake.xgit.site.pojo;

import java.util.Map;

public class SiteUserTable extends SiteDoc {

	private Map<String, SiteUserInfo> users;

	public Map<String, SiteUserInfo> getUsers() {
		return users;
	}

	public void setUsers(Map<String, SiteUserInfo> users) {
		this.users = users;
	}

}
