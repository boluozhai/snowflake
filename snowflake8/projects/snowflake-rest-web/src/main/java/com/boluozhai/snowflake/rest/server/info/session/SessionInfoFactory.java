package com.boluozhai.snowflake.rest.server.info.session;

import javax.servlet.http.HttpServletRequest;

public interface SessionInfoFactory {

	SessionInfo create(HttpServletRequest request);

}
