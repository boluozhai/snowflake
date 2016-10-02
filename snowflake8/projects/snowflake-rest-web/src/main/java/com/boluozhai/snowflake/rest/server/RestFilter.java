package com.boluozhai.snowflake.rest.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;
import com.boluozhai.snowflake.core.SnowflakeException;

public class RestFilter implements Filter {

	private RestRequestHandler _next_handler;

	@Override
	public void init(FilterConfig config) throws ServletException {

		final String key = "handler";
		final String bean_id = config.getInitParameter(key);

		if (bean_id == null) {
			String msg = "the handler id not set: " + key;
			throw new SnowflakeException(msg);
		}

		ServletContext sc = config.getServletContext();
		SnowflakeContext context = SnowContextUtils
				.getWebApplicationContext(sc);
		this._next_handler = context.getBean(bean_id, RestRequestHandler.class);

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest http_request = (HttpServletRequest) request;
		HttpServletResponse http_response = (HttpServletResponse) response;
		this._next_handler.handle(http_request, http_response);

	}

	@Override
	public void destroy() {
	}

}
