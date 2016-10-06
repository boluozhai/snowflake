package com.boluozhai.snowflake.installer.min;

import java.io.File;

import com.boluozhai.snowflake.installer.min.context.ApplicationContext;

public class InstallerContext {

	private final ApplicationContext context;

	public InstallerContext(ApplicationContext context) {
		this.context = context;
	}

	private String getRequiredSystemProperty(String name) {
		String value = System.getProperty(name, null);
		if (value == null) {
			String msg = "no system.property named: " + name;
			throw new RuntimeException(msg);
		}
		return value;
	}

	public File getInstalledWarFile() {
		// TODO Auto-generated method stub

		InstallerConfig conf = InstallerConfig.getInstance(context);
		String war_name = conf.getRemote().getName();

		String name = "wtp.deploy";
		String value = this.getRequiredSystemProperty(name);
		File node = new File(value);
		return new File(node, war_name + ".war");
	}

	public File getCachedWarFile() {
		// TODO Auto-generated method stub

		InstallerConfig conf = InstallerConfig.getInstance(context);
		String war_name = conf.getRemote().getName();

		String name = "catalina.base";
		String value = this.getRequiredSystemProperty(name);
		File node = new File(value);
		return new File(node, war_name + ".cached.war");
	}

	public ApplicationContext getApplicationContext() {
		return context;
	}

}
