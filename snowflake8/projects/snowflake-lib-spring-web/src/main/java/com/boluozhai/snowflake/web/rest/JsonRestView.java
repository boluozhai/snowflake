package com.boluozhai.snowflake.web.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonRestView extends RestView {

	interface Key {

		String pojo = "response.JSON.POJO";

	}

	private Object responsePOJO;

	public Object getResponsePOJO() {
		return responsePOJO;
	}

	public void setResponsePOJO(Object responsePOJO) {
		this.responsePOJO = responsePOJO;
	}

	@Override
	public void forward(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {

		Object obj = this.responsePOJO;
		if (obj == null) {
			obj = request.getAttribute(Key.pojo);
			if (obj == null) {
				obj = new ObjectForNull("the json-object is null.",
						this.getClass());
			}
		}

		GsonBuilder gsb = new GsonBuilder();
		Gson gs = gsb.create();

		String enc = "utf-8";
		String json = gs.toJson(obj);
		byte[] ba = json.getBytes(enc);

		response.setCharacterEncoding(enc);
		response.setContentLength(ba.length);
		response.setContentType("application/json");
		ServletOutputStream out = response.getOutputStream();
		out.write(ba);

	}

	@Override
	public void include(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		this.forward(request, response);
	}

	public static class ObjectForNull {

		private String error;
		private String target;

		public ObjectForNull() {
		}

		public ObjectForNull(String string, Class<?> tar) {
			this.error = string;
			this.target = tar.getName();
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getTarget() {
			return target;
		}

		public void setTarget(String target) {
			this.target = target;
		}

	}

}
