package com.boluozhai.snowflake.spring;

import org.springframework.context.ApplicationContext;

public interface SpringContextLoader {

	String property_key = SpringContextLoader.class.getName() + "_xml";

	ApplicationContext get();

	ApplicationContext load();

}
