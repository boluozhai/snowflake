package com.boluozhai.snowflake.core;

public class SnowflakeException extends RuntimeException {

	private static final long serialVersionUID = 2800924777308456597L;

	public SnowflakeException() {
		super();
	}

	public SnowflakeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SnowflakeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SnowflakeException(String message) {
		super(message);
	}

	public SnowflakeException(Throwable cause) {
		super(cause);
	}

}
