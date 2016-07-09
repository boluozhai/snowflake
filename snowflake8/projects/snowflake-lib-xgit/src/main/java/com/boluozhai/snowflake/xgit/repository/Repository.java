package com.boluozhai.snowflake.xgit.repository;

import java.net.URI;

public interface Repository {

	RepositoryContext context();

	RepositoryDriver driver();

	URI location();

}
