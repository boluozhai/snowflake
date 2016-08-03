package com.boluozhai.snowflake.libwebapp.server;

import java.util.Properties;

import com.boluozhai.snowflake.libwebapp.impl.RootApplicationServerAgent;

public abstract class ApplicationServerAgent {

	public abstract ApplicationServer getServer();

	public static ApplicationServerAgent getAgent() {
		Properties pro = System.getProperties();
		return new RootApplicationServerAgent(pro);
	}

}
