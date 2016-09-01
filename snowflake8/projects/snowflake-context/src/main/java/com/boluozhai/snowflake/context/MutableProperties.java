package com.boluozhai.snowflake.context;

public interface MutableProperties extends SnowflakeProperties {

	void setProperty(String key, String value);

}
