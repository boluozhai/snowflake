package com.boluozhai.snowflake.xgit.http.server.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.rest.server.support.handler.RestHandlerSwitchExpression;
import com.boluozhai.snowflake.xgit.http.server.GitHttpInfo;

public class InfoRefsSwitchExpression implements RestHandlerSwitchExpression {

	@Override
	public String getValue(HttpServletRequest request) throws ServletException,
			IOException {

		GitHttpInfo info = GitHttpInfo.Agent.get(request);

		if ("info/refs".equals(info.resource)) {
			// ok, continue
		} else {
			String msg = "bad path : " + info.resource;
			throw new SnowflakeException(msg);
		}

		return request.getParameter("service");

	}

}
