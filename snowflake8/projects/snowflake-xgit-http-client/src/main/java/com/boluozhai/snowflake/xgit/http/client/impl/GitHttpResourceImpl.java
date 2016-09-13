package com.boluozhai.snowflake.xgit.http.client.impl;

import java.net.URI;

import com.boluozhai.snowflake.xgit.http.client.GitHttpResource;
import com.boluozhai.snowflake.xgit.http.client.GitHttpService;
import com.boluozhai.snowflake.xgit.http.client.RepositoryConnection;

final class GitHttpResourceImpl implements GitHttpResource {

	private static class Inner {

		private final RepositoryConnection repo;
		private final String path;
		private final URI uri;

		private Inner(RepositoryConnection aRepo, String aPath) {
			this.repo = aRepo;
			this.path = aPath;
			this.uri = make_uri(aRepo, aPath);
		}

		private static URI make_uri(RepositoryConnection aRepo, String aPath) {

			final URI base = aRepo.getLocation();
			final String s1 = base.toString();
			final String s2 = aPath;
			final StringBuilder sb = new StringBuilder();

			final boolean b1 = s1.endsWith("/");
			final boolean b2 = s2.startsWith("/");

			if (b1) {
				if (b2) {
					sb.append(s1).append(s2.substring(1));
				} else {
					sb.append(s1).append(s2);
				}
			} else {
				if (b2) {
					sb.append(s1).append(s2);
				} else {
					sb.append(s1).append('/').append(s2);
				}
			}

			return URI.create(sb.toString());
		}

	}

	private final Inner inner;

	public GitHttpResourceImpl(RepositoryConnection repo, String path) {
		this.inner = new Inner(repo, path);
	}

	@Override
	public RepositoryConnection getOwner() {
		return inner.repo;
	}

	@Override
	public URI getURI() {
		return inner.uri;
	}

	@Override
	public String getPath() {
		return inner.path;
	}

	@Override
	public GitHttpService getService(String name) {
		return new GitHttpServiceImpl(this, name);
	}

}
