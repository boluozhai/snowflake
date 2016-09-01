package com.boluozhai.snowflake.libwebapp.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.boluozhai.snowflake.libwebapp.server.ApplicationServerInfo;

public class CommonAppServerInfoBuilder {

	private final Map<String, String> _map;
	private final Properties _properties;

	public CommonAppServerInfoBuilder(Properties properties) {
		this._map = new HashMap<String, String>();
		this._properties = properties;
	}

	public ApplicationServerInfo create() {

		Map<String, String> map = this._map;

		// locate webapps dir
		String base_path = map
				.get(ApplicationServerInfo.prop_key.server_base_dir);
		File webapps_dir = new File(base_path, "webapps");
		this.put(ApplicationServerInfo.prop_key.server_webapps_dir,
				webapps_dir.getAbsolutePath());

		return new CommonAppServerInfo(map);
	}

	public void init() {
		this.mappingKey(ApplicationServerInfo.prop_key.os_arch);
		this.mappingKey(ApplicationServerInfo.prop_key.os_name);
		this.mappingKey(ApplicationServerInfo.prop_key.os_version);
	}

	public void mappingKey(String from_key, String to_key) {
		String value = this._properties.getProperty(to_key);
		this._map.put(from_key, value);
	}

	public void mappingKey(String key) {
		String value = this._properties.getProperty(key);
		this._map.put(key, value);
	}

	public void put(String key, String value) {
		this._map.put(key, value);
	}

}
