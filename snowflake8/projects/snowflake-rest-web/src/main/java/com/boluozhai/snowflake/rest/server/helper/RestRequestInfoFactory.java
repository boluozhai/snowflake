package com.boluozhai.snowflake.rest.server.helper;

import javax.servlet.http.HttpServletRequest;

public interface RestRequestInfoFactory {

	RestRequestInfo getInfo(HttpServletRequest request);

}
