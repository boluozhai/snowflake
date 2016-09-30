package com.boluozhai.snowflake.xgit.http.client.smart.fetch;

import java.io.IOException;

import com.boluozhai.snowflake.xgit.ObjectId;

public interface SmartFetchController {

	void add(ObjectId id);

	void load() throws IOException;

}
