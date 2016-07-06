package com.boluozhai.snowflake.spring.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.boluozhai.snow.util.SystemAttributes;

final class SpringContextLoader {

	private ApplicationContext cache;

	public ApplicationContext load() {
		ApplicationContext ac = null;
		String key = "spring-application-context.xml";
		String path = SystemAttributes.get(key);
		ac = new ClassPathXmlApplicationContext(path);
		return ac;
	}

	public synchronized ApplicationContext get() {
		ApplicationContext ac = cache;
		if (ac == null) {
			ac = load();
			cache = ac;
		}
		return ac;
	}

}
