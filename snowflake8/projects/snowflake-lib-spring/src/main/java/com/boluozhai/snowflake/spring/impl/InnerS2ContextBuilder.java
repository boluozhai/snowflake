package com.boluozhai.snowflake.spring.impl;

import org.springframework.context.ApplicationContext;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.base.AbstractContextBuilder;
import com.boluozhai.snowflake.spring.SpringSnowContextBuilder;

final class InnerS2ContextBuilder extends AbstractContextBuilder implements
		SpringSnowContextBuilder {

	private ApplicationContext _spring;

	protected InnerS2ContextBuilder(SnowContext parent) {
		super(parent);
	}

	@Override
	public ApplicationContext getSpringContext() {
		return this._spring;
	}

	@Override
	public void setSpringContext(ApplicationContext ac) {
		this._spring = ac;
	}

	@Override
	public SnowContext create() {
		SnowContext inner = super.create();
		return new InnerS2Context(inner, _spring);
	}

}
