package com.boluozhai.snowflake.rest.api.h2o;

import com.boluozhai.snowflake.rest.api.RestDoc;
import com.boluozhai.snowflake.rest.element.auth.AuthInfo;

public class AuthModel extends RestDoc {

	private AuthInfo auth;

	public AuthInfo getAuth() {
		return auth;
	}

	public void setAuth(AuthInfo auth) {
		this.auth = auth;
	}

}
