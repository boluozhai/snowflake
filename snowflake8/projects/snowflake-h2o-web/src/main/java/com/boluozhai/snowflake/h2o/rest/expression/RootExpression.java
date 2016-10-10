package com.boluozhai.snowflake.h2o.rest.expression;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.server.support.handler.RestHandlerSwitchExpression;

public class RootExpression implements RestHandlerSwitchExpression {

	@Override
	public String getValue(HttpServletRequest request) throws ServletException,
			IOException {

		String service = request.getParameter("service");

		if (service == null) {
			service = "null";
		}

		return service;
	}

}
