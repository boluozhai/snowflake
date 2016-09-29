package com.boluozhai.snowflake.xgit.http.client.smart;

import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface SmartClientFactory {

	SmartClient create();

	void setLocalRepository(Repository repo);

	void setRemoteRepository(GitHttpRepo repo);

	void setDefaultResource(String value);

	void setDefaultService(String value);

}
