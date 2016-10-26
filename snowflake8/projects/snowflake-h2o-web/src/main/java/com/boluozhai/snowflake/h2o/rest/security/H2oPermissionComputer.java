package com.boluozhai.snowflake.h2o.rest.security;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.access.security.web.PermissionComputer;

public class H2oPermissionComputer implements PermissionComputer {

	@Override
	public String compute(HttpServletRequest request) {

		// TODO Auto-generated method stub
		// return null;

		return "test_permission";

	}

}
