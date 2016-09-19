package com.boluozhai.snowflake.datatable;

public interface DAO {

	String getKey(Object object);

	void setKey(String key, Object object);

	Object makePrototype(Object object);

}
