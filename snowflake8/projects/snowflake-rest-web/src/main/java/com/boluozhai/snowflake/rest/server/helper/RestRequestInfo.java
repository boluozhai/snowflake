package com.boluozhai.snowflake.rest.server.helper;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.server.impl.DefaultRestRequestInfoFactory;

public interface RestRequestInfo {

	public class Factory {

		public static RestRequestInfo getInstance(HttpServletRequest request) {
			RestRequestInfoFactory factory = new DefaultRestRequestInfoFactory();
			return factory.getInfo(request);
		}

	}

}
