package com.boluozhai.snowflake.libwebapp.impl;

import java.util.Properties;

import com.boluozhai.snowflake.libwebapp.server.ApplicationServer;
import com.boluozhai.snowflake.libwebapp.server.ApplicationServerInfo;

public class TheJettyServer implements ApplicationServer {

	private Properties _prop;

	public TheJettyServer(Properties properties) {
		this._prop = properties;
	}

	@Override
	public ApplicationServerInfo getInfo() {

		CommonAppServerInfoBuilder builder = new CommonAppServerInfoBuilder(
				this._prop);

		builder.init();

		builder.put(ApplicationServerInfo.prop_key.server_name, "jetty");

		builder.mappingKey(ApplicationServerInfo.prop_key.server_base_dir,
				"jetty.base");

		builder.mappingKey(ApplicationServerInfo.prop_key.server_version,
				"jetty.version");

		return builder.create();

	}

}
