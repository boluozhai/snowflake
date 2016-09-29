package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClient;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClientFactory;
import com.boluozhai.snowflake.xgit.repository.Repository;

final class SmartClientFactoryImpl implements SmartClientFactory {

	private final InnerSmartClientConfig config;

	public SmartClientFactoryImpl() {
		this.config = new InnerSmartClientConfig();
	}

	@Override
	public SmartClient create() {
		return new SmartClientImpl(config);
	}

	@Override
	public void setLocalRepository(Repository repo) {
		config.local = repo;
	}

	@Override
	public void setRemoteRepository(GitHttpRepo repo) {
		config.remote = repo;
	}

	@Override
	public void setDefaultResource(String value) {
		config.resource = value;
	}

	@Override
	public void setDefaultService(String value) {
		config.service = value;
	}

}
