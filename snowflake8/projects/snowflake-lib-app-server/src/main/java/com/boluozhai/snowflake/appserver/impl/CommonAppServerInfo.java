package com.boluozhai.snowflake.appserver.impl;

import java.io.File;
import java.util.Map;
import java.util.Set;

import com.boluozhai.snowflake.appserver.ApplicationServerInfo;

public class CommonAppServerInfo implements ApplicationServerInfo {

	private final Map<String, String> _map;

	public CommonAppServerInfo(Map<String, String> map) {
		this._map = map;
	}

	@Override
	public String getProperty(String key) {
		return this._map.get(key);
	}

	@Override
	public Set<String> getPropertyNames() {
		return this._map.keySet();
	}

	@Override
	public File getWebappsDir() {
		String key = ApplicationServerInfo.prop_key.server_webapps_dir;
		String path = this.getProperty(key);
		return new File(path);
	}

	@Override
	public File getServerBaseDir() {
		String key = ApplicationServerInfo.prop_key.server_base_dir;
		String path = this.getProperty(key);
		return new File(path);
	}
}
