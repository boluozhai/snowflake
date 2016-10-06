package com.boluozhai.snowflake.installer.min.context;

public interface ApplicationContext {

	<T> T getBean(String key, Class<T> clazz);

}
