package com.boluozhai.snowflake.xgit.http.client.smart;

import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface SmartClientConfigSetter {

	void setLocalRepository(Repository repo);

	void setRemoteRepository(GitHttpRepo repo);

	void setDefaultResource(String resource);

	void setDefaultService(String service);

}
