package com.boluozhai.snowflake.rest.server.support.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.path.PathPart;
import com.boluozhai.snowflake.rest.server.RestRequestHandler;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;

public class RestWwwProxy implements RestRequestHandler {

	private final static String q_service = "?service=www";

	private List<String> keywords;
	private Set<String> _keyword_set;

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	private Set<String> get_keyword_set() {
		Set<String> set = this._keyword_set;
		if (set == null) {
			set = new HashSet<String>(this.keywords);
			set = Collections.synchronizedSet(set);
			this._keyword_set = set;
		}
		return set;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Set<String> kws = this.get_keyword_set();
		MyPathParser pp = new MyPathParser(kws);

		pp.parse(request);

		if (pp.need_redirect()) {
			pp.do_redirect(request, response);
		} else {
			pp.do_forward(request, response);
		}

	}

	private static class MyPathParser {

		private final Set<String> keywords;
		private boolean _is_end_with_slash;
		private boolean _is_end_with_keyword;
		private String[] _parts_in_app;

		public MyPathParser(Set<String> kws) {
			this.keywords = kws;
		}

		public void parse(HttpServletRequest request) {

			final String uri = request.getRequestURI();

			RestRequestInfo rr_info = RestRequestInfo.Factory
					.getInstance(request);
			PathInfo p_info = rr_info.getPathInfo();
			PathPart inapp_path = p_info.getInAppPart();
			String[] array = inapp_path.trimToArray();

			String last_part = null;
			if (array.length > 0) {
				last_part = array[array.length - 1];
			} else {
				last_part = "";
			}

			this._is_end_with_keyword = this.keywords.contains(last_part);
			this._is_end_with_slash = uri.endsWith("/");
			this._parts_in_app = array;

		}

		public void do_forward(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {

			String[] parts = this._parts_in_app;
			ArrayList<String> list = new ArrayList<String>();
			for (String p : parts) {
				list.add(p);
			}

			if (this._is_end_with_keyword) {
				int i = list.size() - 1;
				String end = list.get(i);
				end = end + ".html" + q_service;
				list.set(i, end);
			} else {
				list.add("index.html" + q_service);
			}

			for (int i = 0; i < list.size() - 1; i++) {
				String s = list.get(i);
				if (i == 0) {
					s = "user";
				} else if (i == 1) {
					s = "repo";
				} else {
					break;
				}
				list.set(i, s);
			}

			StringBuilder sb = new StringBuilder();
			for (String s : list) {
				sb.append('/').append(s);
			}
			String path = sb.toString();

			System.out.format("    forward to ~%s\n", path);

			request.getRequestDispatcher(path).forward(request, response);

		}

		public void do_redirect(HttpServletRequest request,
				HttpServletResponse response) {

			String url = request.getRequestURI();

			if (this._is_end_with_slash) {
				url = url.substring(0, url.length() - 1);
			} else {
				url = url + '/';
			}

			System.out.format("    redirect to %s\n", url);

			response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
			response.setHeader("Location", url);

		}

		public boolean need_redirect() {
			return (this._is_end_with_keyword == this._is_end_with_slash);
		}
	}

}
