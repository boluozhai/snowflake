package com.boluozhai.snowflake.rest.api.h2o;

import com.boluozhai.snowflake.rest.api.RestDoc;
import com.boluozhai.snowflake.rest.element.auth.AuthProfile;

public class AuthModel extends RestDoc {

	private AuthProfile request;
	private AuthProfile response;

	public AuthProfile getRequest() {
		return request;
	}

	public void setRequest(AuthProfile request) {
		this.request = request;
	}

	public AuthProfile getResponse() {
		return response;
	}

	public void setResponse(AuthProfile response) {
		this.response = response;
	}

}
