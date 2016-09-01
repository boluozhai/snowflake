package com.boluozhai.snowflake.context;

public interface MutableAttributes extends SnowflakeAttributes {

	void setAttribute(String key, Object value);

}
