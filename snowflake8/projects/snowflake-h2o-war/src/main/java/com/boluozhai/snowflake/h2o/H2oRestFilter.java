package com.boluozhai.snowflake.h2o;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.server.RestFilter;

public class H2oRestFilter extends RestFilter {

	private String[] _static_prefix;
	private String _root_path;

	public H2oRestFilter() {
	}

	public void destroy() {
		super.destroy();
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (this.is_static_res(request)) {
			chain.doFilter(request, response);
		} else {
			super.doFilter(request, response, chain);
		}

	}

	private boolean is_static_res(ServletRequest request) {

		HttpServletRequest http_request = (HttpServletRequest) request;
		String path = URI.create(http_request.getRequestURI()).getPath();

		System.out.println("path = " + path);

		if (path.equals(this._root_path)) {
			return true;
		}

		String[] array = this._static_prefix;
		for (String s : array) {
			if (path.startsWith(s)) {
				return true;
			}
		}
		return false;
	}

	private List<String> get_static_res_list() {

		List<String> list = new ArrayList<String>();
		list.add("admin");
		list.add("css");
		list.add("export");
		list.add("image");
		list.add("js");
		list.add("lib");
		list.add("sign");
		list.add("system");
		list.add("test");
		list.add("www");
		return list;

	}

	public void init(FilterConfig conf) throws ServletException {
		super.init(conf);

		String cp = conf.getServletContext().getContextPath();
		List<String> list = this.get_static_res_list();
		List<String> s2 = new ArrayList<String>();

		for (String s : list) {
			String prefix = cp + '/' + s + '/';
			s2.add(prefix);
		}

		this._static_prefix = s2.toArray(new String[s2.size()]);
		this._root_path = cp + '/';

	}

}
