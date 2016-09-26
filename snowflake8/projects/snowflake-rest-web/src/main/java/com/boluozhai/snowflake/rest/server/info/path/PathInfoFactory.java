package com.boluozhai.snowflake.rest.server.info.path;

import javax.servlet.http.HttpServletRequest;

public interface PathInfoFactory {

	PathInfo create(HttpServletRequest request);

}
