package com.boluozhai.snowflake.xgit.site;

import java.net.URI;

import com.boluozhai.snowflake.xgit.repository.Repository;

public interface SystemRepository extends Repository {

	RepositoryInfo getRepositoryInfo(URI uri);

	String[] listNames();

}
