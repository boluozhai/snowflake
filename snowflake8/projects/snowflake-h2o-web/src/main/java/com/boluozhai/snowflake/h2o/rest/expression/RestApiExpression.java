package com.boluozhai.snowflake.h2o.rest.expression;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.rest.server.support.handler.RestHandlerSwitchExpression;

public class RestApiExpression implements RestHandlerSwitchExpression {

	@Override
	public String getValue(HttpServletRequest request) throws ServletException,
			IOException {

		RestRequestInfo rest_info = RestRequestInfo.Factory
				.getInstance(request);
		PathInfo path_info = rest_info.getPathInfo();
		String value = path_info.getRequiredPart("type").toString("");
		return value;

	}

}
