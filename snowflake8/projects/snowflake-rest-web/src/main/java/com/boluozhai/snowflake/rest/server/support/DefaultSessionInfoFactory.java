package com.boluozhai.snowflake.rest.server.support;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.server.info.session.SessionInfo;
import com.boluozhai.snowflake.rest.server.info.session.SessionInfoFactory;

public class DefaultSessionInfoFactory implements SessionInfoFactory {

	@Override
	public SessionInfo create(HttpServletRequest request) {
		return new DefaultSessionInfo(request);
	}

}
