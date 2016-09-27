package com.boluozhai.snowflake.rest.server.support.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface RestHandlerSwitchExpression {

	String getValue(HttpServletRequest request) throws ServletException,
			IOException;

}
