package com.boluozhai.snowflake.xgit.http.client.impl;

import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.support.DefaultXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.support.OpenRepositoryParam;
import com.boluozhai.snowflake.xgit.support.RepositoryLoader;

final class HttpRepositoryBuilder implements RepositoryLoader {

	public HttpRepositoryBuilder() {
	}

	@Override
	public Repository load(OpenRepositoryParam param) {
		InnerLoader loader = new InnerLoader();
		return loader.load(param);
	}

	private class InnerLoader extends DefaultXGitComponentBuilder {
	}

}
