package com.boluozhai.snowflake.rest.server.support.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestRequestHandler;

public interface RestRequestFilter {

	void doFilter(HttpServletRequest request, HttpServletResponse response,
			RestRequestHandler next) throws ServletException, IOException;

}
