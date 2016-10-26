package com.boluozhai.snowflake.access.security.web;

import javax.servlet.http.HttpServletRequest;

public interface RoleComputer {

	String[] compute(HttpServletRequest request);

}
