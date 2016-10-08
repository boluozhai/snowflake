package com.boluozhai.snowflake.rest.api.h2o;

import com.boluozhai.snowflake.rest.api.RestDoc;
import com.boluozhai.snowflake.rest.element.session.SessionProfile;

public class SessionModel extends RestDoc {

	private SessionProfile session;

	public SessionModel() {
		this.session = new SessionProfile();
	}

	public SessionProfile getSession() {
		return session;
	}

	public void setSession(SessionProfile session) {
		this.session = session;
	}

}
