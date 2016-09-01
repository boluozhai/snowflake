package com.boluozhai.snowflake.context;

import java.net.URI;

public interface SnowflakeContextMeta {

	String getName();

	String getDescription();

	long getBirthday();

	URI getURI();

	SnowflakeContext getParent();

	Object getBean(String name);

	<T> T getBean(String name, Class<T> type);

}
