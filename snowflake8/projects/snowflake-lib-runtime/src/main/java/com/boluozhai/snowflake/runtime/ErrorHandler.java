package com.boluozhai.snowflake.runtime;

public interface ErrorHandler extends LineHandler {

	void onError(SubProcess sp, Throwable e);

}
