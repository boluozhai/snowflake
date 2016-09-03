package com.boluozhai.snowflake.rest.server;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestServlet.RestInfo;

public class RestController implements RequestDispatcher {

	private final void dispatch(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String method = request.getMethod();

		if (method == null) {

		} else if (method.equalsIgnoreCase("GET")) {
			this.rest_get(request, response);

		} else if (method.equalsIgnoreCase("PUT")) {
			this.rest_put(request, response);

		} else if (method.equalsIgnoreCase("POST")) {
			this.rest_post(request, response);

		} else if (method.equalsIgnoreCase("DELETE")) {
			this.rest_delete(request, response);

		} else {
		}

	}

	public final RestInfo getRestInfo(ServletRequest request) {
		return RestServlet.getRestInfo(request);
	}

	@Override
	public final void forward(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		this.dispatch(request, response);
	}

	@Override
	public final void include(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		this.dispatch(request, response);
	}

	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void rest_put(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void rest_delete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}