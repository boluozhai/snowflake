package com.boluozhai.snowflake.rest.server.impl;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.server.helper.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.helper.RestRequestInfoFactory;

public class DefaultRestRequestInfoFactory implements RestRequestInfoFactory {

	private final static String key = DefaultRestRequestInfoFactory.class
			.getName() + ".bind";

	@Override
	public RestRequestInfo getInfo(HttpServletRequest request) {

		RestRequestInfo info = (RestRequestInfo) request.getAttribute(key);
		if (info == null) {
			info = new RestRequestInfoImpl(request);
			request.setAttribute(key, info);
		}
		return info;
	}

}
