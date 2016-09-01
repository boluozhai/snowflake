package com.boluozhai.snowflake.appdata.support;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.appdata.AppDataAgent;

public class DefaultAppDataAgent extends AppDataAgent {

	private final AppDataAgent _inner;

	public DefaultAppDataAgent() {
		this._inner = new StaticAppDataAgent();
	}

	@Override
	public AppData getAppData(Class<?> schema) {
		return this.getAppData(schema, true);
	}

	@Override
	public AppData getAppData(Class<?> schema, boolean throw_exception) {
		return _inner.getAppData(schema, throw_exception);
	}

}
