package com.boluozhai.snowflake.spring.utils;

import org.springframework.context.ApplicationContext;

import com.boluozhai.snowflake.context.ContextBuilder;

public class SpringContextBuilderConfigHelper {

	public static void config(ContextBuilder builder, ApplicationContext ac) {

		String[] keys = ac.getBeanDefinitionNames();
		for (String key : keys) {
			Object bean = ac.getBean(key);
			builder.setAttribute(key, bean);
		}

		String key = ApplicationContext.class.getName();
		builder.setAttribute(key, ac);

	}

}
