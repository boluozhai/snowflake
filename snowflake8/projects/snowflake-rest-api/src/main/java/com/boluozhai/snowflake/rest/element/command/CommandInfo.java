package com.boluozhai.snowflake.rest.element.command;

public class CommandInfo {

	private String command;
	private String pathURI; // like 'file:///xxx'
	private String message;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getPathURI() {
		return pathURI;
	}

	public void setPathURI(String pathURI) {
		this.pathURI = pathURI;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
