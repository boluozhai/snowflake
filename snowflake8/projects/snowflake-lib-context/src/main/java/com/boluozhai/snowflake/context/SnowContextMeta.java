package com.boluozhai.snowflake.context;

import java.net.URI;

public interface SnowContextMeta {

	String getName();

	String getDescription();

	long birthday();

	URI getURI();

	SnowContext getParent();

	Object getBean(String name);

	<T> T getBean(String name, Class<T> type);

}
