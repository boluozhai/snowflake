package com.boluozhai.snowflake.xgit.http.client.smart;

public interface SmartClientBuilder extends SmartClientConfigSetter,
		SmartClientConfigGetter {

	SmartClient create();

}
