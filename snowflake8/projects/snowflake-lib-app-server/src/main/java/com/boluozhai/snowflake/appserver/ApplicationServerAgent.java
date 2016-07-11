package com.boluozhai.snowflake.appserver;

import java.util.Properties;

import com.boluozhai.snowflake.appserver.impl.RootApplicationServerAgent;

public abstract class ApplicationServerAgent {

	public abstract ApplicationServer getServer();

	public static ApplicationServerAgent getAgent() {
		Properties pro = System.getProperties();
		return new RootApplicationServerAgent(pro);
	}

}
