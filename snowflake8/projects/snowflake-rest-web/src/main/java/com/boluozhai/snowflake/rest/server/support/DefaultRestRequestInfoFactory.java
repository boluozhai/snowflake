package com.boluozhai.snowflake.rest.server.support;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.server.impl.RestInfoImplementation;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfoFactory;

public class DefaultRestRequestInfoFactory extends
		AbstractRestRequestInfoFactory {

	@Override
	public RestRequestInfo getInstance(HttpServletRequest request) {
		AbstractRestRequestInfoFactory abs = this;
		RestRequestInfoFactory factory = RestInfoImplementation
				.getInfoFactory(abs);
		return factory.getInstance(request);
	}

}
