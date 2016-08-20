package com.boluozhai.snowflake.context;

public interface MutableParameters extends SnowflakeParameters {

	void setParameter(String key, String value);

}
