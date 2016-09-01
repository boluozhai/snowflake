package com.boluozhai.snowflake.runtime.impl;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.runtime.ErrorHandler;
import com.boluozhai.snowflake.runtime.LineHandler;
import com.boluozhai.snowflake.runtime.SubProcess;
import com.boluozhai.snowflake.runtime.SubProcessBuilder;

public class SubProcessBuilderImpl implements SubProcessBuilder {

	private final InnerSPInfo _info = new InnerSPInfo();

	@Override
	public SnowflakeContext getContext() {
		return this._info.context;
	}

	@Override
	public String getCommand() {
		return this._info.command;
	}

	@Override
	public String[] getCommandLines() {
		return InnerSPInfo.toStringArray(_info.command);
	}

	@Override
	public LineHandler getOutputHandler() {
		return this._info.h_output;
	}

	@Override
	public ErrorHandler getErrorHandler() {
		return _info.h_error;
	}

	@Override
	public Writer getInputWriter() {
		throw new RuntimeException("unsupported");
	}

	@Override
	public void setContext(SnowflakeContext context) {
		_info.context = context;
	}

	@Override
	public void setCommand(String cmd) {
		_info.command = cmd;
	}

	@Override
	public void setCommand(String[] cmd) {
		StringBuilder sb = new StringBuilder();
		for (String s : cmd) {
			sb.append(s).append('\n');
		}
		_info.command = sb.toString();
	}

	@Override
	public void setOutputHandler(LineHandler h) {
		_info.h_output = h;
	}

	@Override
	public void setErrorHandler(ErrorHandler h) {
		_info.h_error = h;
	}

	@Override
	public SubProcess create() {
		InnerSPInfo info = _info.make_copy();
		return new SubProcessImpl(info);
	}

}
