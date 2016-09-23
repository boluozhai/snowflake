package com.boluozhai.snowflake.datatable;

import com.boluozhai.snowflake.xgit.ObjectId;

public interface DataLine {

	String getUser();

	String getHost();

	ObjectId getId();

	/**
	 * key='type:name'
	 * */

	<T> T insert(T obj);

	<T> T insertOrUpdate(T obj);

	boolean exists(Class<?> type);

	<T> T update(T obj);

	boolean delete(Object obj);

	<T> T get(Class<T> type);

}
