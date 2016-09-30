package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClient;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClientBuilder;
import com.boluozhai.snowflake.xgit.repository.Repository;

final class SmartClientBuilderImpl implements SmartClientBuilder {

	private String service;
	private String resource;
	private GitHttpRepo remote;
	private Repository local;

	public SmartClientBuilderImpl() {
	}

	@Override
	public SmartClient create() {
		return new SmartClientImpl(this);
	}

	@Override
	public void setLocalRepository(Repository repo) {
		this.local = repo;
	}

	@Override
	public void setRemoteRepository(GitHttpRepo repo) {
		this.remote = repo;
	}

	@Override
	public void setDefaultResource(String value) {
		this.resource = value;
	}

	@Override
	public void setDefaultService(String value) {
		this.service = value;
	}

	@Override
	public Repository getLocalRepository() {
		return this.local;
	}

	@Override
	public GitHttpRepo getRemoteRepository() {
		return this.remote;
	}

	@Override
	public String getDefaultResource() {
		return this.resource;
	}

	@Override
	public String getDefaultService() {
		return this.service;
	}

}
