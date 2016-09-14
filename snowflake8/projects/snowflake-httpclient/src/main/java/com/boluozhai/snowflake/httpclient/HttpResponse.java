package com.boluozhai.snowflake.httpclient;

public class HttpResponse extends HttpMessage {

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

}
