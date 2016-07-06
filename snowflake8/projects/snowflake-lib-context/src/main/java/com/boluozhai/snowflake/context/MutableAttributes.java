package com.boluozhai.snowflake.context;

public interface MutableAttributes extends SnowAttributes {

	void setAttribute(String key, Object value);

}
