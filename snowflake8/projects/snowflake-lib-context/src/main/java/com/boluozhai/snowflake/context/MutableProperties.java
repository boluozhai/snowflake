package com.boluozhai.snowflake.context;

public interface MutableProperties extends SnowProperties {

	void setProperty(String key, String value);

}
