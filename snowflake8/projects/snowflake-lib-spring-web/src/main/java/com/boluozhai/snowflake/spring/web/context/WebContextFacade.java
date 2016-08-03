package com.boluozhai.snowflake.spring.web.context;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.support.ContextWrapper;

public class WebContextFacade extends ContextWrapper implements
		WebContext {

	private final WebApplicationContext _spring_web_context;

	public WebContextFacade(SnowContext inner, WebApplicationContext spring) {
		super(inner);
		this._spring_web_context = spring;
	}

	@Override
	public WebApplicationContext getSpringWebContext() {
		return this._spring_web_context;
	}

	@Override
	public ApplicationContext getSpringContext() {
		return this._spring_web_context;
	}

}
