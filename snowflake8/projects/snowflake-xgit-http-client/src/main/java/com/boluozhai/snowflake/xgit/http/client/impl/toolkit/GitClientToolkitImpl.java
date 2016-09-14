package com.boluozhai.snowflake.xgit.http.client.impl.toolkit;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.http.client.toolkit.GitClientToolkit;
import com.boluozhai.snowflake.xgit.http.client.toolkit.RemoteAgent;

final class GitClientToolkitImpl implements GitClientToolkit {

	private final SnowflakeContext _context;

	public GitClientToolkitImpl(SnowflakeContext context) {
		this._context = context;
	}

	@Override
	public RemoteAgent getRemote(String url) {
		return new RemoteAgentImpl(_context, url);
	}

}
