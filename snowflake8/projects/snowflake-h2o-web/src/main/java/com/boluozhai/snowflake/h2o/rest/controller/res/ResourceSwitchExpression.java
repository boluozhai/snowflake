package com.boluozhai.snowflake.h2o.rest.controller.res;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.path.PathPart;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.support.handler.RestHandlerSwitchExpression;

public class ResourceSwitchExpression implements RestHandlerSwitchExpression {

	@Override
	public String getValue(HttpServletRequest request) throws ServletException,
			IOException {

		RestRequestInfo info = RestRequestInfo.Factory.getInstance(request);
		PathPart path = info.getPathInfo().getInAppPart();
		return path.toString("~/");

	}

}
