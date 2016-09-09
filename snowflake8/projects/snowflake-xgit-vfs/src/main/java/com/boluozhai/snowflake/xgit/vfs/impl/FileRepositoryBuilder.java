package com.boluozhai.snowflake.xgit.vfs.impl;

import java.io.IOException;
import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.support.DefaultXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.support.RepositoryProfile;

public class FileRepositoryBuilder {

	private DefaultXGitComponentBuilder _inner;
	private RepositoryOption _option;

	public FileRepositoryBuilder(SnowflakeContext context) {
		this._inner = new DefaultXGitComponentBuilder(context);
	}

	public Repository create() {

		this.inner_load_default_values();

		ComponentContext cc = _inner.create();

		Repository repo = cc.getBean(XGitContext.component.repository,
				Repository.class);
		Config conf = cc.getBean(XGitContext.component.config, Config.class);

		if (this._option.check_config) {
			this.check(conf);
		}

		return repo;
	}

	private void inner_load_default_values() {

		if (this._option == null) {
			this._option = new RepositoryOption();
			this._inner.setOption(_option);
		}

	}

	private void check(Config conf) {

		try {
			conf.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

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
