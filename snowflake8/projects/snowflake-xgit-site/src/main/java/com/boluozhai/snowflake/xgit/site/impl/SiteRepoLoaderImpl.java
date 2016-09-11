package com.boluozhai.snowflake.xgit.site.impl;

import java.net.URI;

import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.support.OpenRepositoryParam;
import com.boluozhai.snowflake.xgit.support.RepositoryLoader;
import com.boluozhai.snowflake.xgit.vfs.impl.FileRepositoryBuilder;

final class SiteRepoLoaderImpl implements RepositoryLoader {

	public static RepositoryLoader newLoader() {
		return new SiteRepoLoaderImpl();
	}

	@Override
	public Repository load(OpenRepositoryParam param) {
		Loading ld = new Loading(param);
		ld.prepare_default_value();
		ld.check_uri();
		return ld.done();
	}

	private class Loading {

		private final OpenRepositoryParam param;

		public Loading(OpenRepositoryParam param) {
			this.param = param;
		}

		public void check_uri() {
			final URI uri = param.uri;
			final String scheme = uri.getScheme();
			if ("file".equals(scheme)) {
				// ok
			} else {
				String msg = "the URI [%s] need a 'file' scheme.";
				msg = String.format(msg, uri);
				throw new RuntimeException(msg);
			}
		}

		public void prepare_default_value() {
			RepositoryOption option = param.option;
			if (option == null) {
				option = new RepositoryOption();
				param.option = option;
			}
		}

		public Repository done() {
			RepositoryLoader loader = new FileRepositoryBuilder();
			return loader.load(param);
		}
	}

}
