package com.boluozhai.snowflake.rest.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

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

		String enc = "utf-8";
		String mime = "application/json";

		Model pojo = new Model();
		pojo.setMessage(this.exception.getMessage());
		pojo.setError(this.exception.toString());

		Gson gs = new Gson();
		String str = gs.toJson(pojo);
		byte[] ba = str.getBytes(enc);

		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setCharacterEncoding(enc);
		resp.setContentType(mime);
		resp.setContentLength(ba.length);
		resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		ServletOutputStream out = resp.getOutputStream();
		out.write(ba);

	}

	public static class Model {

		private String error;
		private String message;

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

}
