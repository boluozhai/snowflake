package com.boluozhai.snowflake.h2o.rest.security;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.access.security.web.RoleComputer;

public class H2oRoleComputer implements RoleComputer {

	@Override
	public String[] compute(HttpServletRequest request) {
		// TODO Auto-generated method stub
		// return null;

		String[] array = { "tester" };
		return array;
	}

}
