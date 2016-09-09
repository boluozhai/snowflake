package com.boluozhai.snowflake.rest.client;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface RestMessage {

	SnowflakeContext getContext();

	RestEntity getEntity();

	void setEntity(RestEntity entity);

}
