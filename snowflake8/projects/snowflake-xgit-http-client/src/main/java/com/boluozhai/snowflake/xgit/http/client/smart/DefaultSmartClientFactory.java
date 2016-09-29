package com.boluozhai.snowflake.xgit.http.client.smart;

import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.http.client.smart.impl.SmartClientImplementation;
import com.boluozhai.snowflake.xgit.repository.Repository;

public class DefaultSmartClientFactory implements SmartClientFactory {

	private final SmartClientFactory inner;

	public DefaultSmartClientFactory() {
		this.inner = SmartClientImplementation.getClientFactory();
	}

	@Override
	public SmartClient create() {
		return inner.create();
	}

	@Override
	public void setLocalRepository(Repository repo) {
		inner.setLocalRepository(repo);
	}

	@Override
	public void setRemoteRepository(GitHttpRepo repo) {
		inner.setRemoteRepository(repo);
	}

	@Override
	public void setDefaultResource(String value) {
		inner.setDefaultResource(value);
	}

	@Override
	public void setDefaultService(String value) {
		inner.setDefaultService(value);
	}

}
