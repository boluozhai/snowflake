package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.repository.Repository;

final class InnerSmartClientConfig {

	public Repository local;
	public GitHttpRepo remote;
	public String resource;
	public String service;

}
