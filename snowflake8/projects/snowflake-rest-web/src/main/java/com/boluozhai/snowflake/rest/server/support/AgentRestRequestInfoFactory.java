package com.boluozhai.snowflake.rest.server.support;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfoFactory;

public class AgentRestRequestInfoFactory implements RestRequestInfoFactory {

	private final static String binding_key = AgentRestRequestInfoFactory.class
			.getName() + ".binding";

	private final static String factory_key = AgentRestRequestInfoFactory.class
			.getName() + ".factory";

	private RestRequestInfo create_info(HttpServletRequest request) {

		final String key = factory_key;

		RestRequestInfoFactory factory = (RestRequestInfoFactory) request
				.getAttribute(key);
		if (factory == null) {
			throw new RuntimeException(
					"no factory binded to the request, with attr-name : " + key);
		}
		return factory.getInstance(request);
	}

	@Override
	public RestRequestInfo getInstance(HttpServletRequest request) {

		final String key = binding_key;

		RestRequestInfo info = (RestRequestInfo) request.getAttribute(key);
		if (info == null) {
			info = this.create_info(request);
			request.setAttribute(key, info);
		}
		return info;
	}

}
