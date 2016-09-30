package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import java.io.IOException;

import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClient;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClientConfigGetter;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartTx;
import com.boluozhai.snowflake.xgit.repository.Repository;

final class SmartClientImpl implements SmartClient {

	private final Repository local_repo;
	private final GitHttpRepo remote_repo;
	private final String resource_default;
	private final String service_default;

	public SmartClientImpl(SmartClientConfigGetter config) {
		this.remote_repo = config.getRemoteRepository();
		this.local_repo = config.getLocalRepository();
		this.resource_default = config.getDefaultResource();
		this.service_default = config.getDefaultService();
	}

	@Override
	public Repository getLocalRepository() {
		return this.local_repo;
	}

	@Override
	public GitHttpRepo getRemoteRepository() {
		return this.remote_repo;
	}

	@Override
	public String getDefaultResource() {
		return this.resource_default;
	}

	@Override
	public String getDefaultService() {
		return this.service_default;
	}

	@Override
	public SmartTx openTx() throws IOException {
		return this.openTx(this.resource_default, this.service_default);
	}

	@Override
	public SmartTx openTx(String resource, String service) throws IOException {
		InnerSmartCore core = new InnerSmartCore(this, resource, service);
		return new SmartTxImpl(core);
	}

}
