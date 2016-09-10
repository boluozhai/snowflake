package com.boluozhai.snowflake.xgit.support;

import com.boluozhai.snowflake.xgit.repository.Repository;

public interface RepositoryLoader {

	Repository load(OpenRepositoryParam param);

}
