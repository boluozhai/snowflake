package com.boluozhai.snowflake.datatable;

import java.io.Closeable;

import com.boluozhai.snowflake.core.SnowflakeException;

public interface DataClient extends Closeable {

	Transaction beginTransaction() throws SnowflakeException;

	DataLine line(String name, String type);

	DataLine line(String name, Class<?> type);

	DataLine line(String key);

	String[] list(Class<?> type);

	DataClientFactory getFactory();

}
