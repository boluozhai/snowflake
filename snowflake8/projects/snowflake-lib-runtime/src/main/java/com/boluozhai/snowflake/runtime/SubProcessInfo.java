package com.boluozhai.snowflake.runtime;

import java.io.Writer;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface SubProcessInfo {

	SnowflakeContext getContext();

	String getCommand();

	String[] getCommandLines();

	LineHandler getOutputHandler();

	ErrorHandler getErrorHandler();

	Writer getInputWriter();

}
