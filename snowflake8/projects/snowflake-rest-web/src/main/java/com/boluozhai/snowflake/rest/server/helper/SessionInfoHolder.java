package com.boluozhai.snowflake.rest.server.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.boluozhai.snowflake.rest.api.h2o.SessionModel;

public class SessionInfoHolder {

	private final static String key = SessionInfoHolder.class.getName()
			+ ".binding";

	private final HttpServletRequest request;

	private SessionInfoHolder(HttpServletRequest aRequest) {
		this.request = aRequest;
	}

	public static SessionInfoHolder create(HttpServletRequest aRequest) {
		return new SessionInfoHolder(aRequest);
	}

	public SessionModel get() {
		HttpSession session = this.get_session();
		SessionModel model = (SessionModel) session.getAttribute(key);
		if (model == null) {
			model = new SessionModel();
		}
		return model;
	}

	private HttpSession get_session() {
		return request.getSession();
	}

	public void set(SessionModel model) {
		HttpSession session = this.get_session();
		session.setAttribute(key, model);
	}

}
