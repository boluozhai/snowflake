package com.boluozhai.snowflake.cli;

import java.util.Map;

public final class CommandSet {

	private boolean enable;
	private String prefix;
	private String suffix;
	private Map<String, CLICommandHandler> commands;

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Map<String, CLICommandHandler> getCommands() {
		return commands;
	}

	public void setCommands(Map<String, CLICommandHandler> commands) {
		this.commands = commands;
	}

}
