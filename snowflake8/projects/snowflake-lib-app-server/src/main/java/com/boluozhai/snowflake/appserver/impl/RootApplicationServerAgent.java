package com.boluozhai.snowflake.appserver.impl;

import java.util.Properties;

import com.boluozhai.snowflake.appserver.ApplicationServer;
import com.boluozhai.snowflake.appserver.ApplicationServerAgent;

public class RootApplicationServerAgent extends ApplicationServerAgent {

	private ApplicationServer _server = null;

	public interface Keys {

		String tomcat_base = "catalina.base";
		String tomcat_home = "catalina.home";
		String tomcat_version = "catalina.version";

		String jetty_base = "jetty.base";
		String jetty_home = "jetty.home";
		String jetty_version = "jetty.version";

		String os_name = "os.name";

	}

	private static class PropertyGetter {

		private Properties _pro;

		public PropertyGetter(Properties properties) {
			this._pro = properties;
		}

		public String get(String key) {
			return _pro.getProperty(key);
		}
	}

	public RootApplicationServerAgent(Properties properties) {

		PropertyGetter sp = new PropertyGetter(properties);

		String tomcat_base = sp.get(Keys.tomcat_base);
		// String tomcat_home = sp.get(Keys.tomcat_home);
		// String tomcat_version = sp.get(Keys.tomcat_version);

		String jetty_base = sp.get(Keys.jetty_base);
		// String jetty_home = sp.get(Keys.jetty_home);
		// String jetty_version = sp.get(Keys.jetty_version);

		String os_name = sp.get(Keys.os_name);

		if (os_name == null) {
			throw new RuntimeException("bad System.property");
		} else if (jetty_base != null) {
			this._server = new TheJettyServer(properties);
		} else if (tomcat_base != null) {
			this._server = new TheTomcatServer(properties);
		} else {
			throw new RuntimeException("unknow System.property");
		}

	}

	@Override
	public ApplicationServer getServer() {
		return this._server;
	}

}
