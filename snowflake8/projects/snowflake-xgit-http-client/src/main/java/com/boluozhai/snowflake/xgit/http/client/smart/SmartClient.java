package com.boluozhai.snowflake.xgit.http.client.smart;

import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface SmartClient {

	Repository getLocalRepository();

	GitHttpRepo getRemoteRepository();

	String getDefaultResource();

	String getDefaultService();

	SmartTx openTx();

	SmartTx openTx(String resource, String service);

}
