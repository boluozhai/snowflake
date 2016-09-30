package com.boluozhai.snowflake.xgit.http.client.smart;

import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface SmartClientConfigGetter {

	Repository getLocalRepository();

	GitHttpRepo getRemoteRepository();

	String getDefaultResource();

	String getDefaultService();

}
