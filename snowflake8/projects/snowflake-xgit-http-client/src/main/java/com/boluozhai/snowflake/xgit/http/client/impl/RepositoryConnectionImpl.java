package com.boluozhai.snowflake.xgit.http.client.impl;

import java.net.URI;

import com.boluozhai.snowflake.xgit.http.client.GitHttpClient;
import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.http.client.GitHttpResource;

final class RepositoryConnectionImpl implements GitHttpRepo {

	private final Inner inner;

	private RepositoryConnectionImpl(Inner in) {
		this.inner = in;
	}

	public static GitHttpRepo open(URI uri, GitHttpClient client) {
		Inner inner = new Inner(uri, client);
		inner.init();
		return new RepositoryConnectionImpl(inner);
	}

	private static class Inner {

		private final GitHttpClient _client;
		private final URI _uri;

		public Inner(URI uri, GitHttpClient client) {
			this._uri = uri;
			this._client = client;
		}

		public void init() {
			// TODO Auto-generated method stub

		}
	}

	@Override
	public GitHttpClient getClient() {
		return inner._client;
	}

	@Override
	public URI getURI() {
		return inner._uri;
	}

	@Override
	public GitHttpResource getResource(String path) {
		return new GitHttpResourceImpl(this, path);
	}

}
