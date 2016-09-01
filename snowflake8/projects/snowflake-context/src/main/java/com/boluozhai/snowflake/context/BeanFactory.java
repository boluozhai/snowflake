package com.boluozhai.snowflake.context;

public interface BeanFactory {

	Object getBean(String name);

	<T> T getBean(String name, Class<T> type);

}
