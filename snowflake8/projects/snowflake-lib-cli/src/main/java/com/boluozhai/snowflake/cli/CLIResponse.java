package com.boluozhai.snowflake.cli;

import java.io.OutputStream;

import com.boluozhai.snowflake.context.MutableAttributes;
import com.boluozhai.snowflake.context.SnowContext;

public interface CLIResponse {

	public static class Agent {

		private final static String key = CLIResponse.class.getName()
				+ "@agent";

		public static CLIResponse getResponse(SnowContext context) {
			return context.getBean(key, CLIResponse.class);
		}

		public static void setResponse(MutableAttributes context,
				CLIResponse response) {
			context.setAttribute(key, response);
		}

	}

	OutputStream getOutputStream();

	void setError(Throwable err);

	void setOutputText(String text);

	void setOutputObject(Object object);

}
