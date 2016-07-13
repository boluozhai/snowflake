package com.boluozhai.snowflake.xgit.support;

import com.boluozhai.snowflake.xgit.repository.RepositoryDriver;

public abstract class AbstractRepositoryDriver implements RepositoryDriver {

	private RepositoryProfile profile;

	public RepositoryProfile getProfile() {
		return profile;
	}

	public void setProfile(RepositoryProfile profile) {
		this.profile = profile;
	}

}
