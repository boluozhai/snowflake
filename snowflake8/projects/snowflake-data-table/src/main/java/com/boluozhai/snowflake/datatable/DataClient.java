package com.boluozhai.snowflake.datatable;

import java.io.Closeable;

import com.boluozhai.snowflake.datatable.pojo.Model;

public interface DataClient extends Closeable {

	Transaction beginTransaction();

	<T extends Model> T insert(String name, T obj);

	<T extends Model> T insertOrUpdate(String name, T obj);

	<T extends Model> T update(T obj);

	<T extends Model> T get(String name, Class<T> type);

	boolean delete(Model obj);

}
