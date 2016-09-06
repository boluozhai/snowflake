package com.boluozhai.snowflake.rest.api.h2o;

import com.boluozhai.snowflake.rest.api.RestDoc;
import com.boluozhai.snowflake.rest.element.command.CommandInfo;

public class CommandModel extends RestDoc {

	private CommandInfo argument;
	private CommandInfo result;

	public CommandInfo getArgument() {
		return argument;
	}

	public void setArgument(CommandInfo argument) {
		this.argument = argument;
	}

	public CommandInfo getResult() {
		return result;
	}

	public void setResult(CommandInfo result) {
		this.result = result;
	}

}
