package com.boluozhai.snowflake.xgit.http.client.smart.fetch;

public interface SmartFetchItemController {

	void check();

	boolean exists();

	boolean loaded();

	void setLoaded();

}
