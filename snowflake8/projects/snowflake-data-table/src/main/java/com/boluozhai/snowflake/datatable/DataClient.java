package com.boluozhai.snowflake.datatable;

import java.io.Closeable;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.xgit.ObjectId;

public interface DataClient extends Closeable {

	Transaction beginTransaction() throws SnowflakeException;

	<T> T insert(T obj);

	<T> T update(T obj);

	<T> boolean delete(ObjectId id, Class<T> type);

	<T> boolean delete(Object obj);

	<T> T get(ObjectId id, Class<T> type);

	<T> T get(ObjectId id, Class<T> type, boolean canBeNil);

	<T> T get(String name, Class<T> type);

	<T> T get(String name, Class<T> type, boolean canBeNil);

	DataClientFactory getFactory();

}
