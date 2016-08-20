package com.boluozhai.snowflake.context;

public interface MutableEnvironments extends SnowflakeEnvironments {

	void setEnvironment(String key, String value);

}
