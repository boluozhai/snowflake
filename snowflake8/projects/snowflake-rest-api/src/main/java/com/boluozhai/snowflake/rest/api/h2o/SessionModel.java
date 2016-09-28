package com.boluozhai.snowflake.rest.api.h2o;

import com.boluozhai.snowflake.rest.api.RestDoc;
import com.boluozhai.snowflake.rest.element.session.SessionParam;

public class SessionModel extends RestDoc {

	private SessionParam session;

	public SessionModel() {
		this.session = new SessionParam();
	}

	public SessionParam getSession() {
		return session;
	}

	public void setSession(SessionParam session) {
		this.session = session;
	}

}
