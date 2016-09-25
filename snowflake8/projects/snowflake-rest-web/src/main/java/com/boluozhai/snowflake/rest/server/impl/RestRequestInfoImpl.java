package com.boluozhai.snowflake.rest.server.impl;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.server.helper.RestRequestInfo;

final class RestRequestInfoImpl implements RestRequestInfo {

	private final HttpServletRequest request;

	public RestRequestInfoImpl(HttpServletRequest request) {
		this.request = request;
	}

}
