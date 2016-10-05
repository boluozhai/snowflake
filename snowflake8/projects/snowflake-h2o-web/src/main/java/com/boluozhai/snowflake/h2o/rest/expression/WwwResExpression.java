package com.boluozhai.snowflake.h2o.rest.expression;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.server.support.handler.RestHandlerSwitchExpression;

public class WwwResExpression implements RestHandlerSwitchExpression {

	@Override
	public String getValue(HttpServletRequest request) throws ServletException,
			IOException {

		String uri = request.getRequestURI();
		int index = uri.lastIndexOf('/');
		if (index < 0) {
			return uri;
		} else {
			return uri.substring(index + 1);
		}

	}

}
