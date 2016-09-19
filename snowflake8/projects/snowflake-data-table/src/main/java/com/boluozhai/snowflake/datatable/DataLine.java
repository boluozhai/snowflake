package com.boluozhai.snowflake.datatable;

public interface DataLine {

	Class<?> getTypeClass();

	String getType();

	String getName();

	/**
	 * key='type:name'
	 * */

	String getKey();

	<T> T insert(T obj);

	<T> T update(T obj);

	boolean delete();

	boolean exists();

	<T> T get(Class<T> type);

}
