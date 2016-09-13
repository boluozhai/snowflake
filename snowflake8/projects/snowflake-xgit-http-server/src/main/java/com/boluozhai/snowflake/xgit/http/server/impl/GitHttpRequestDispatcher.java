package com.boluozhai.snowflake.xgit.http.server.impl;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.xgit.http.server.GitHttpController;
import com.boluozhai.snowflake.xgit.http.server.GitHttpInfo;
import com.boluozhai.snowflake.xgit.http.server.GitHttpServlet;
import com.boluozhai.snowflake.xgit.http.server.controller.NullCtrl;

public class GitHttpRequestDispatcher {

	private final String _repo_path_offset; // like '/repo/*'

	public GitHttpRequestDispatcher(HttpServlet servlet) {

		String key = "repo-path-offset";
		String value = servlet.getInitParameter(key);
		if (value == null) {
			String msg = "need init-param[%s]";
			msg = String.format(msg, key);
			throw new RuntimeException(msg);
		} else {
			this._repo_path_offset = value;
		}

	}

	public void process(HttpServletRequest rx, HttpServletResponse tx,
			Map<String, GitHttpController> tab) throws ServletException,
			IOException {

		ServletContext sc = rx.getServletContext();
		final String url = rx.getRequestURL().toString();

		PathParser pp = new PathParser(url);
		// /{context}/{repo}/{res}
		pp.load_context(sc.getContextPath());
		pp.load_repo(this._repo_path_offset);

		// make info
		GitHttpInfo info = new GitHttpInfo();
		info.service = rx.getParameter("service");
		if (pp.path_ok()) {
			info.url = pp.get_repo_url();
			info.name = pp.get_repo_name();
			info.resource = pp.get_res_path();
		}

		GitHttpInfo.Agent.put(info, rx);

		// dispatch
		GitHttpController next = this.get_next_ctrl(info, tab);
		next.forward(rx, tx);

	}

	private GitHttpController get_next_ctrl(GitHttpInfo info,
			Map<String, GitHttpController> tab) {

		String key = null;
		if (info.name == null) {
			key = GitHttpServlet.SERVICE.FORBIDDEN;
		} else {
			String res = info.resource;

			if (res == null) {
				key = GitHttpServlet.SERVICE.HOME;

			} else if (res.equals("")) {
				key = GitHttpServlet.SERVICE.HOME;

			} else if (res.equals("info/refs")) {

				// by service
				if (info.service == null) {
					key = GitHttpServlet.SERVICE.FORBIDDEN;
				} else {
					key = info.service;
				}

			} else {
				key = GitHttpServlet.SERVICE.FORBIDDEN;
			}
		}

		GitHttpController next = tab.get(key);
		if (next == null) {
			next = new NullCtrl();
		}
		return next;
	}

	private static class PathParser {

		private final PathBuilder pb = new PathBuilder();

		private final String _host_etc;

		private String[] _repo_array;
		private String[] _context_array;
		private String[] _full_array;

		public PathParser(String url) {

			final URI uri = URI.create(url);
			final String p2 = uri.getPath();
			final int i = url.indexOf(p2);
			final String p1 = url.substring(0, i);

			this._host_etc = p1;

			this.load_full(p2);
		}

		public boolean path_ok() {
			int a1 = this._context_array.length;
			int a2 = this._repo_array.length;
			int a3 = this._full_array.length;
			return ((a1 + a2) <= a3);
		}

		public String get_repo_name() {
			int i = this._context_array.length + this._repo_array.length - 1;
			return this._full_array[i];
		}

		public String get_res_path() {
			StringBuilder sb = new StringBuilder();
			int from = this._context_array.length + this._repo_array.length;
			int to = this._full_array.length;
			for (int i = from; i < to; i++) {
				String s = this._full_array[i];
				if (sb.length() > 0) {
					sb.append("/");
				}
				sb.append(s);
			}
			return sb.toString();
		}

		public String get_repo_url() {

			StringBuilder sb = new StringBuilder();
			sb.append(this._host_etc);

			int from = 0;
			int to = this._context_array.length + this._repo_array.length;

			for (int i = from; i < to; i++) {
				String s = this._full_array[i];
				sb.append('/').append(s);
			}
			return sb.toString();
		}

		public void load_repo(String repo_temp) {
			pb.reset();
			pb.appendElements(repo_temp);
			this._repo_array = pb.toArray();
		}

		public void load_context(String context_temp) {
			pb.reset();
			pb.appendElements(context_temp);
			this._context_array = pb.toArray();
		}

		public void load_full(String path) {
			pb.reset();
			pb.appendElements(path);
			this._full_array = pb.toArray();
		}

	}

}
