package com.boluozhai.snowflake.libwebapp.update.publisher.command.helper;

import java.io.OutputStream;

import com.boluozhai.snowflake.cli.CLIResponse;

public class CliPojoResponse implements CLIResponse {

	private Object outputObject;

	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unsupports");
	}

	@Override
	public void setError(Throwable err) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unsupports");
	}

	@Override
	public void setOutputText(String text) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unsupports");
	}

	@Override
	public void setOutputObject(Object object) {
		this.outputObject = object;
	}

	public Object getOutputObject() {
		return outputObject;
	}

}
