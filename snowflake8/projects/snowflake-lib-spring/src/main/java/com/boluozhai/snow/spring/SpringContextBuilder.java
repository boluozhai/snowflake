package com.boluozhai.snow.spring;

import org.springframework.context.ApplicationContext;

import com.boluozhai.snow.context.SnowContext;
import com.boluozhai.snow.context.support.AbstractContextBuilder;

public class SpringContextBuilder extends AbstractContextBuilder {

	private static SnowContext cache;
	private final ApplicationContext _spring_context;

	public SpringContextBuilder(SnowContext parent, ApplicationContext ac) {
		super(parent);
		this._spring_context = ac;
	}

	@Override
	public SnowContext create() {
		SnowContext sc = cache;
		if (sc == null) {
			sc = this.create_impl();
			cache = sc;
		}
		return sc;
	}

	private final SnowContext create_impl() {

		ApplicationContext spring = this._spring_context;

		String name = spring.getApplicationName();
		String desc = spring.getDisplayName();
		this.setName(name);
		this.setDescription(desc);

		String[] list = spring.getBeanDefinitionNames();
		for (String n : list) {
			Object bean = spring.getBean(n);
			this.setAttribute(n, bean);
		}

		return super.create();
	}

}
