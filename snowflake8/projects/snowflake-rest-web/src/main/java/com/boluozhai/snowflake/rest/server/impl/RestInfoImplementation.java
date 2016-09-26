package com.boluozhai.snowflake.rest.server.impl;

import com.boluozhai.snowflake.rest.server.info.RestRequestInfoFactory;
import com.boluozhai.snowflake.rest.server.support.AbstractRestRequestInfoFactory;

public final class RestInfoImplementation {

	public static RestRequestInfoFactory getInfoFactory(
			AbstractRestRequestInfoFactory abs) {

		return new RestRequestInfoImpl.Builder(abs);

	}
}
