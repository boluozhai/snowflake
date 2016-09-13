package com.boluozhai.snowflake.xgit.http.server;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GitHttpController implements RequestDispatcher {

	@Override
	public final void forward(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {

		HttpServletRequest rx = (HttpServletRequest) request;
		HttpServletResponse tx = (HttpServletResponse) response;
		String method = rx.getMethod();

		if (method == null) {
			this.include(request, response);

		} else if (method.equalsIgnoreCase("POST")) {
			this.git_post(rx, tx);

		} else if (method.equalsIgnoreCase("GET")) {
			this.git_get(rx, tx);

		} else if (method.equalsIgnoreCase("PUT")) {
			this.git_put(rx, tx);

		} else if (method.equalsIgnoreCase("DELETE")) {
			this.git_delete(rx, tx);

		} else {
			this.include(request, response);
		}

	}

	@Override
	public final void include(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		throw new ServletException("no support");
	}

	public final GitHttpInfo getInfo(ServletRequest request) {
		return GitHttpInfo.Agent.get(request);
	}

	protected void git_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException("no impl");
	}

	protected void git_delete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException("no impl");
	}

	protected void git_put(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException("no impl");
	}

	protected void git_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException("no impl");
	}

}
