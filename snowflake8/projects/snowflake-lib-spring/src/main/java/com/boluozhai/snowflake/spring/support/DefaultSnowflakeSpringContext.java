package com.boluozhai.snowflake.spring.support;

import org.springframework.context.ApplicationContext;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.support.ContextWrapper;
import com.boluozhai.snowflake.spring.SnowflakeSpringContext;

public class DefaultSnowflakeSpringContext extends ContextWrapper implements
		SnowflakeSpringContext {

	private final ApplicationContext _spring_context;

	public DefaultSnowflakeSpringContext(SnowContext inner,
			ApplicationContext spring) {
		super(inner);
		this._spring_context = spring;
	}

	@Override
	public ApplicationContext getSpringContext() {
		return this._spring_context;
	}

}
