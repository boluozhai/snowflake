package com.boluozhai.snowflake.context;

public interface MutableEnvironments extends SnowEnvironments {

	void setEnvironment(String key, String value);

}
