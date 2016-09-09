package com.boluozhai.snowflake.cli.support;

import java.io.PrintStream;

import com.boluozhai.snowflake.cli.CLIResponse;

public class DefaultCliResponse implements CLIResponse {

	@Override
	public PrintStream out() {
		return System.out;
	}

	@Override
	public PrintStream err() {
		return System.err;
	}

}
