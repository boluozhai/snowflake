package com.boluozhai.snowflake.xgit.http.base;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentContext;

public class HttpXGitComponentBuilder implements ComponentBuilder {

	@Override
	public void configure(ContextBuilder cb) {
		// TODO Auto-generated method stub

	}

	@Override
	public Component create(ComponentContext cc) {
		throw new RuntimeException("impl in subclass");
	}

}
