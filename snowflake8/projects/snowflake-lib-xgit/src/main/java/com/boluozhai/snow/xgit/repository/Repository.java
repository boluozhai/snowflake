package com.boluozhai.snow.xgit.repository;

import java.net.URI;

public interface Repository {

	RepositoryContext context();

	RepositoryDriver driver();

	URI location();

}
