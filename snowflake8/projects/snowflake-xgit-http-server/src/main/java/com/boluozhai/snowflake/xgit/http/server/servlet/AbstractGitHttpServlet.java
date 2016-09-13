package com.boluozhai.snowflake.xgit.http.server.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.xgit.http.server.GitHttpController;
import com.boluozhai.snowflake.xgit.http.server.GitHttpServlet;
import com.boluozhai.snowflake.xgit.http.server.impl.GitHttpRequestDispatcher;

public abstract class AbstractGitHttpServlet extends HttpServlet implements
		GitHttpServlet {

	private static final long serialVersionUID = -758015613705264547L;
	private Map<String, GitHttpController> _handlers;
	private GitHttpRequestDispatcher _impl;

	public AbstractGitHttpServlet() {
	}

	private final GitHttpRequestDispatcher inner_get_impl() {
		GitHttpRequestDispatcher impl = this._impl;
		if (impl == null) {
			impl = new GitHttpRequestDispatcher(this);
			this._impl = impl;
		}
		return impl;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.inner_do_all(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.inner_do_all(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.inner_do_all(req, resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.inner_do_all(req, resp);
	}

	private final void inner_do_all(HttpServletRequest rx,
			HttpServletResponse tx) throws ServletException, IOException {
		Map<String, GitHttpController> tab = this.get_handler_table();
		GitHttpRequestDispatcher impl = this.inner_get_impl();
		impl.process(rx, tx, tab);
	}

	private final Map<String, GitHttpController> get_handler_table() {
		Map<String, GitHttpController> tab = this._handlers;
		if (tab == null) {
			tab = this.create_handler_table();
			this._handlers = tab;
		}
		return tab;
	}

	protected abstract Map<String, GitHttpController> create_handler_table();

}
