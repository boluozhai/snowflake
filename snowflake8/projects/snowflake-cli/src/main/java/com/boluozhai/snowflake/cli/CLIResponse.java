package com.boluozhai.snowflake.cli;

import java.io.PrintStream;

import com.boluozhai.snowflake.context.MutableAttributes;
import com.boluozhai.snowflake.context.SnowflakeContext;

public interface CLIResponse {

	public static class Agent {

		private final static String key = CLIResponse.class.getName()
				+ "-agent";

		public static CLIResponse getResponse(SnowflakeContext context) {
			return context.getBean(key, CLIResponse.class);
		}

		public static void setResponse(MutableAttributes context,
				CLIResponse response) {
			context.setAttribute(key, response);
		}

	}

	PrintStream out();

	PrintStream err();

}
