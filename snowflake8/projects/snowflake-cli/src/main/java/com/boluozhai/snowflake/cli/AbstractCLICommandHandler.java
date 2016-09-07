package com.boluozhai.snowflake.cli;

public abstract class AbstractCLICommandHandler implements CLICommandHandler,
		CLICommandDescription {

	private String usage;
	private String description;

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
