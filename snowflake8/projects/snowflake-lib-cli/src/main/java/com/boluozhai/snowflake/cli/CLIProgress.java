package com.boluozhai.snowflake.cli;

import com.boluozhai.snowflake.context.MutableAttributes;
import com.boluozhai.snowflake.context.SnowContext;

public interface CLIProgress {

	public static class Agent {

		private final static String key = CLIProgress.class.getName()
				+ "@agent";

		public static CLIProgress getResponse(SnowContext context) {
			return context.getBean(key, CLIProgress.class);
		}

		public static void setResponse(MutableAttributes context,
				CLIProgress response) {
			context.setAttribute(key, response);
		}

	}

	// TODO ...

}
