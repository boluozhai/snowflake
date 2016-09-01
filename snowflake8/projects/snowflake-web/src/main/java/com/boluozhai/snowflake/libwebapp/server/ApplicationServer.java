package com.boluozhai.snowflake.libwebapp.server;

public interface ApplicationServer {

	ApplicationServerInfo getInfo();

	public class Agent {

		public static ApplicationServer getServer() {
			ApplicationServerAgent agent = ApplicationServerAgent.getAgent();
			return agent.getServer();
		}

	}

}
