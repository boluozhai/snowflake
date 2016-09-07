package com.boluozhai.snowflake.rest.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class JsonRestExceptionView extends RestView {

	private Throwable exception;

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	@Override
	public void forward(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {

		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		ServletOutputStream out = resp.getOutputStream();
		out.println(this.exception.getMessage());

	}
}
