package com.boluozhai.snowflake.rootrepo.support;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rootrepo.RootRepoAgent;
import com.boluozhai.snowflake.rootrepo.RootRepoClient;
import com.boluozhai.snowflake.rootrepo.impl.RootRepoImplementation;

public class DefaultRootRepoAgent implements RootRepoAgent {

	private String repositoryURL;

	public String getRepositoryURL() {
		return repositoryURL;
	}

	public void setRepositoryURL(String repositoryURL) {
		this.repositoryURL = repositoryURL;
	}

	@Override
	public RootRepoClient getClient(SnowflakeContext context) {
		return RootRepoImplementation.newClient(context, this.repositoryURL);
	}

}
