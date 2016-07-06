package com.boluozhai.snowflake.context;

public interface MutableParameters extends SnowParameters {

	void setParameter(String key, String value);

}
