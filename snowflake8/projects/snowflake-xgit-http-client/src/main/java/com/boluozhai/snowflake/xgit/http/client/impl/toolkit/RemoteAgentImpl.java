package com.boluozhai.snowflake.xgit.http.client.impl.toolkit;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.http.client.GitHttpClient;
import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.http.client.toolkit.GitReceivePackRequest;
import com.boluozhai.snowflake.xgit.http.client.toolkit.GitUploadPackRequest;
import com.boluozhai.snowflake.xgit.http.client.toolkit.RemoteAgent;

final class RemoteAgentImpl implements RemoteAgent {

	private final GitHttpRepo _repo;
	private final String _url;

	public RemoteAgentImpl(SnowflakeContext context, String url) {

		GitHttpClient client = GitHttpClient.Factory.getInstance(context);
		this._repo = client.connect(URI.create(url));
		this._url = url;

	}

	@Override
	public String getURL() {
		return this._url;
	}

	@Override
	public GitUploadPackRequest git_upload_pack() {
		return new GitUploadPackRequestImpl(_repo);
	}

	@Override
	public GitReceivePackRequest git_receive_pack() {
		return new GitReceivePackRequestImpl(_repo);
	}

}
