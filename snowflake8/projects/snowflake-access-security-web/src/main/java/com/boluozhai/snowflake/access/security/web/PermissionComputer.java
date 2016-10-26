package com.boluozhai.snowflake.access.security.web;

import javax.servlet.http.HttpServletRequest;

public interface PermissionComputer {

	String compute(HttpServletRequest request);

}
