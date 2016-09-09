package com.boluozhai.snowflake.xgit.http.impl;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.support.DefaultXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.support.RepositoryProfile;

final class HttpRepositoryBuilder {

	private DefaultXGitComponentBuilder _inner;
	private RepositoryOption _option;

	public HttpRepositoryBuilder(SnowflakeContext context) {
		this._inner = new DefaultXGitComponentBuilder(context);
	}

	public Repository create() {

		ComponentContext cc = _inner.create();

		Repository repo = cc.getBean(XGitContext.component.repository,
				Repository.class);
		Config conf = cc.getBean(XGitContext.component.config, Config.class);

		if (this._option.check_config) {
			this.check(conf);
		}

		return repo;
	}

	private void check(Config conf) {

		String key = Config.core.repositoryformatversion;
		String value = conf.getProperty(key);
		if (!"0".equals(value)) {
			String msg = "bad property value, key=%s, value=%s";
			msg = String.format(msg, key, value);
			throw new RuntimeException(msg);
		}

	}

	public void uri(URI uri) {
		_inner.setURI(uri);
	}

	public void option(RepositoryOption option) {
		_inner.setOption(option);
		this._option = option;
	}

	public void profile(RepositoryProfile pf) {
		_inner.setProfile(pf);
	}

}
