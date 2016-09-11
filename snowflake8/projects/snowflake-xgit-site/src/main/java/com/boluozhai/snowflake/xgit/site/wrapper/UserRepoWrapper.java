package com.boluozhai.snowflake.xgit.site.wrapper;

import com.boluozhai.snowflake.xgit.site.UserRepository;

public class UserRepoWrapper extends SiteRepoWrapper implements UserRepository {

	protected final UserRepository inner_user;

	public UserRepoWrapper(UserRepository in) {
		super(in);
		this.inner_user = in;
	}

}
