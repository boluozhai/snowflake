package com.boluozhai.snowflake.rest.server.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.boluozhai.snowflake.rest.api.h2o.SessionModel;
import com.boluozhai.snowflake.rest.server.info.session.SessionInfo;

public class DefaultSessionInfo implements SessionInfo {

	private final static String key = DefaultSessionInfo.class.getName()
			+ ".binding";

	private final HttpServletRequest _request;

	public DefaultSessionInfo(HttpServletRequest request) {
		this._request = request;
	}

	@Override
	public SessionModel getModel() {
		HttpSession session = this._request.getSession(false);
		if (session == null) {
			return new SessionModel();
		} else {
			SessionModel model = (SessionModel) session.getAttribute(key);
			if (model == null) {
				return new SessionModel();
			} else {
				return model;
			}
		}
	}

	@Override
	public void setModel(SessionModel model) {

		if (model == null) {
			return;
		}
		HttpSession session = this._request.getSession(true);
		session.setAttribute(key, model);

	}

}
