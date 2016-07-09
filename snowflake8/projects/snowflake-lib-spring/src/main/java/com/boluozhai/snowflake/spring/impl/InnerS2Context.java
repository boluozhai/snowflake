package com.boluozhai.snowflake.spring.impl;

import org.springframework.context.ApplicationContext;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.support.ContextWrapper;
import com.boluozhai.snowflake.spring.SpringSnowContext;

final class InnerS2Context extends ContextWrapper implements SpringSnowContext {

	private final ApplicationContext _spring_context;

	public InnerS2Context(SnowContext inner, ApplicationContext spring) {
		super(inner);
		this._spring_context = spring;
	}

	@Override
	public ApplicationContext getSpringContext() {
		return this._spring_context;
	}

}
