package com.boluozhai.snowflake.rest.server.info;

import javax.servlet.http.HttpServletRequest;

public interface RestRequestInfoFactory {

	RestRequestInfo getInstance(HttpServletRequest request);

}
