package com.boluozhai.snowflake.runtime;

import java.io.Writer;

import com.boluozhai.snowflake.context.SnowContext;

public interface SubProcessInfo {

	SnowContext getContext();

	String getCommand();

	String[] getCommandLines();

	LineHandler getOutputHandler();

	ErrorHandler getErrorHandler();

	Writer getInputWriter();

}
