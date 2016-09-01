package com.boluozhai.snowflake.libwebapp.server;

import java.io.File;
import java.util.Set;

public interface ApplicationServerInfo {

	interface prop_key {

		String os_arch = "os.arch";
		String os_name = "os.name";
		String os_version = "os.version";

		String server_name = "server.name";
		String server_version = "server.version";
		String server_base_dir = "server.base_dir";
		String server_webapps_dir = "server.webapps_dir";

	}

	String getProperty(String key);

	Set<String> getPropertyNames();

	File getWebappsDir();

	File getServerBaseDir();

}
