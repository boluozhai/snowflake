package com.boluozhai.snowflake.appdata.support;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.appdata.AppDataAgent;

public class DefaultAppDataAgent extends AppDataAgent {

	private final AppDataAgent _inner;

	public DefaultAppDataAgent() {
		this._inner = new StaticAppDataAgent();
	}

	@Override
	public AppData getAppData() {
		return _inner.getAppData();
	}

	@Override
	public AppData getAppData(boolean throw_exception) {
		return _inner.getAppData(throw_exception);
	}

}
