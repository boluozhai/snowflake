package com.boluozhai.snowflake.xgit.http.client.impl;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.httpclient.HttpClient;
import com.boluozhai.snowflake.xgit.http.client.AbstractGitHttpClient;
import com.boluozhai.snowflake.xgit.http.client.GitHttpClient;
import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;

public class GitHttpClientImpl extends AbstractGitHttpClient {

	private final SnowflakeContext _context;
	private HttpClient _http_client;

	private GitHttpClientImpl(SnowflakeContext cc) {
		this._context = cc;
	}

	public static GitHttpClient newInstance(SnowflakeContext context) {
		return new GitHttpClientImpl(context);
	}

	@Override
	public GitHttpRepo connect(URI uri) {
		return RepositoryConnectionImpl.open(uri, this);
	}

	@Override
	public HttpClient getHttpClient() {
		HttpClient htc = this._http_client;
		if (htc == null) {
			htc = HttpClient.Factory.getInstance(_context);
			this._http_client = htc;
		}
		return htc;
	}

}
