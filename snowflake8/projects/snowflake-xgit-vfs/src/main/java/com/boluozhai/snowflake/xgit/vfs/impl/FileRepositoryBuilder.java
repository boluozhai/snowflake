package com.boluozhai.snowflake.xgit.vfs.impl;

import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.support.DefaultXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.support.OpenRepositoryParam;
import com.boluozhai.snowflake.xgit.support.RepositoryLoader;

public class FileRepositoryBuilder implements RepositoryLoader {

	public FileRepositoryBuilder() {
	}

	private final class InnerLoader extends DefaultXGitComponentBuilder {

		public InnerLoader() {
		}

		private void check(Repository repo, OpenRepositoryParam param) {

			// NOP

		}

		private OpenRepositoryParam load_default_values(
				OpenRepositoryParam param) {
			if (param.option == null) {
				param.option = new RepositoryOption();
			}
			return param;
		}

		@Override
		public Repository load(OpenRepositoryParam param) {
			param = this.load_default_values(param);
			Repository repo = super.load(param);
			if (param.option.check_config) {
				this.check(repo, param);
			}
			return repo;
		}

	}

	@Override
	public Repository load(OpenRepositoryParam param) {
		InnerLoader loader = new InnerLoader();
		return loader.load(param);
	}

}
