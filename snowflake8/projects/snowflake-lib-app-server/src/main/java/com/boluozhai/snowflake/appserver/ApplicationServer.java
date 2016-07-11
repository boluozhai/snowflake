package com.boluozhai.snowflake.appserver;

public interface ApplicationServer {

	ApplicationServerInfo getInfo();

	public class Agent {

		public static ApplicationServer getServer() {
			ApplicationServerAgent agent = ApplicationServerAgent.getAgent();
			return agent.getServer();
		}

	}

}
