package com.boluozhai.snowflake.rest.api.h2o;

import com.boluozhai.snowflake.rest.api.RestDoc;
import com.boluozhai.snowflake.rest.element.auth.AuthInfo;

public class AuthModel extends RestDoc {

	private AuthInfo request;
	private AuthInfo response;

	public AuthInfo getRequest() {
		return request;
	}

	public void setRequest(AuthInfo request) {
		this.request = request;
	}

	public AuthInfo getResponse() {
		return response;
	}

	public void setResponse(AuthInfo response) {
		this.response = response;
	}

}
