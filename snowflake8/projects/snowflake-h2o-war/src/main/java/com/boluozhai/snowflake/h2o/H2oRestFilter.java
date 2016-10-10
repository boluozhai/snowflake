package com.boluozhai.snowflake.h2o;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestRequestHandler;
import com.boluozhai.snowflake.rest.server.support.handler.RestRequestFilter;

/*************
 * switch by query parameter named 'service' , the value can be
 * [www|system-api|repo-api|git-upload-pack|...]
 * */

public class H2oRestFilter implements RestRequestFilter {

	private RestRequestHandler nextHandler;
	private List<String> staticResourceSuffix;

	private String[] _static_suffix; // cache

	public H2oRestFilter() {
	}

	public List<String> getStaticResourceSuffix() {
		return staticResourceSuffix;
	}

	public void setStaticResourceSuffix(List<String> staticResourceSuffix) {
		this.staticResourceSuffix = staticResourceSuffix;
	}

	public RestRequestHandler getNextHandler() {
		return nextHandler;
	}

	public void setNextHandler(RestRequestHandler nextHandler) {
		this.nextHandler = nextHandler;
	}

	private boolean is_static_res(HttpServletRequest request) {

		final String uri = request.getRequestURI();
		System.out.println("URI = " + uri);

		final String service = request.getParameter("service");
		if (service == null) {
			// continue
		} else if (service.equals("www")) {
			return true;
		} else {
			return false;
		}

		String[] array = this.get_static_res_suffix_list();
		for (String suffix : array) {
			if (uri.endsWith(suffix)) {
				return true;
			}
		}

		return false;
	}

	private String[] get_static_res_suffix_list() {
		String[] array = this._static_suffix;
		if (array == null) {
			List<String> list = this.staticResourceSuffix;
			array = list.toArray(new String[list.size()]);
			this._static_suffix = array;
		}
		return array;
	}

	@Override
	public void doFilter(HttpServletRequest request,
			HttpServletResponse response, RestRequestHandler next)
			throws ServletException, IOException {

		if (this.is_static_res(request)) {
			// static
			next.handle(request, response);
		} else {
			// active
			this.nextHandler.handle(request, response);
		}

	}

}
