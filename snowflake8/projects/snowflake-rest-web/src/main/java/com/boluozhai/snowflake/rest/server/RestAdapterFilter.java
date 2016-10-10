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
import com.boluozhai.snowflake.rest.server.support.handler.RestRequestFilter;

public class RestAdapterFilter implements Filter {

	private RestRequestFilter _inner;

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
		this._inner = context.getBean(bean_id, RestRequestFilter.class);

	}

	private RestRequestFilter get_inner(ServletRequest request) {
		return this._inner;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest http_request = (HttpServletRequest) request;
		HttpServletResponse http_response = (HttpServletResponse) response;

		RestRequestFilter inner = this.get_inner(request);
		inner.doFilter(http_request, http_response, new MyChainWrapper(chain));

	}

	private static class MyChainWrapper implements RestRequestHandler {

		private final FilterChain chain;

		public MyChainWrapper(FilterChain chain) {
			this.chain = chain;
		}

		@Override
		public void handle(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}

}
