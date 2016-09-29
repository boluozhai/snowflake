package com.boluozhai.snowflake.xgit.http.server.controller.utils;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.utils.RepositoryAgent;

public class ServiceHelper {

	public static Repository forRepository(HttpServletRequest request) {
		RestRequestInfo reqinfo = RestRequestInfo.Factory.getInstance(request);
		SnowflakeContext context = reqinfo.getContext();
		RepositoryAgent agent = RepositoryAgent.Factory.get(context);
		return agent.getRepository(context);
	}

}
