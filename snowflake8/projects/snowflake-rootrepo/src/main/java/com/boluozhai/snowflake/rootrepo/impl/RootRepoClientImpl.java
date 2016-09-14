package com.boluozhai.snowflake.rootrepo.impl;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rootrepo.RootRepoClient;
import com.boluozhai.snowflake.rootrepo.method.DoTest;
import com.boluozhai.snowflake.xgit.http.client.GitHttpClient;
import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.http.client.GitHttpService;

final class RootRepoClientImpl implements RootRepoClient {

	private final SnowflakeContext _context;
	private final GitHttpRepo _root_repo;
	private final String _url;

	public RootRepoClientImpl(SnowflakeContext context, String url) {
		this._context = context;
		this._url = url;
		this._root_repo = Inner.make_gh_repo(context, url);
	}

	private static class Inner {

		public static GitHttpRepo make_gh_repo(SnowflakeContext context,
				String url) {
			GitHttpClient client = GitHttpClient.Factory.getInstance(context);
			return client.connect(URI.create(url));
		}

		public static GitHttpService get_service(RootRepoClientImpl self,
				String res, String serv) {
			return self._root_repo.getResource(res).getService(serv);
		}

	}

	public static RootRepoClient create(SnowflakeContext context,
			String repositoryURL) {

		if (repositoryURL == null) {
			String msg = "the property [%s] not setted.";
			msg = String.format(msg, "repositoryURL");
			throw new RuntimeException(msg);
		}

		return new RootRepoClientImpl(context, repositoryURL);

	}

	@Override
	public GitHttpRepo getRootRepository() {
		return this._root_repo;
	}

	@Override
	public DoTest doTest() {
		GitHttpService service = Inner.get_service(this, "info/refs", "test");
		return new DoTest(_context, service);
	}

}
