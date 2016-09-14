package com.boluozhai.snowflake.httpclient;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import com.boluozhai.snowflake.util.IOTools;

public class HttpResponse extends HttpMessage implements Closeable {

	private HttpRequest request;
	private int code;
	private String message;

	public HttpRequest getRequest() {
		return request;
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void close() throws IOException {
		HttpEntity ent = this.getEntity();
		InputStream in = ent.input();
		IOTools.close(in);
	}

}
