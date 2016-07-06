package com.boluozhai.snowflake.spring.impl;

import org.springframework.context.ApplicationContext;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.base.AbstractContextBuilder;
import com.boluozhai.snowflake.spring.support.DefaultSnowflakeSpringContext;
import com.boluozhai.snowflake.spring.utils.SpringBuilderMaker;

public class SpringContextBuilder extends AbstractContextBuilder {

	private final static SpringContextLoader spring_loader;

	static {
		spring_loader = new SpringContextLoader();
	}

	protected SpringContextBuilder(SnowContext parent) {
		super(parent);
	}

	public static ContextBuilder newInstance(SnowContext parent) {
		return new SpringContextBuilder(parent);
	}

	@Override
	public SnowContext create() {
		ApplicationContext spring = spring_loader.load();
		SpringBuilderMaker.make(this, spring);
		SnowContext sc = super.create();
		return new DefaultSnowflakeSpringContext(sc, spring);
	}
}
