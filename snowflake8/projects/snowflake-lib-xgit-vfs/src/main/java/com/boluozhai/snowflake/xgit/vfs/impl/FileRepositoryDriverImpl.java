package com.boluozhai.snowflake.xgit.vfs.impl;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryDriver;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.support.AbstractRepositoryDriver;
import com.boluozhai.snowflake.xgit.support.RepositoryProfile;

public class FileRepositoryDriverImpl extends AbstractRepositoryDriver
		implements RepositoryDriver {

	public FileRepositoryDriverImpl(RepositoryProfile pf) {
		this.setProfile(pf);
	}

	@Override
	public Repository open(SnowflakeContext context, URI uri, RepositoryOption option) {
		RepositoryProfile pf = this.getProfile();
		FileRepositoryBuilder builder = new FileRepositoryBuilder(context, pf);
		return builder.create(uri, option);
	}

	@Override
	public RepositoryLocator getLocator() {
		RepositoryProfile prof = this.getProfile();
		return new FileRepositoryLocatorImpl(prof);
	}

}
