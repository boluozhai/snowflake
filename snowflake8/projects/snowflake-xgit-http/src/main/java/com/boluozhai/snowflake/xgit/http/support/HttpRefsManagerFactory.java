package com.boluozhai.snowflake.xgit.http.support;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.xgit.http.impl.XHttpRefsImpl;

public class HttpRefsManagerFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return XHttpRefsImpl.newBuilder();
	}

}
