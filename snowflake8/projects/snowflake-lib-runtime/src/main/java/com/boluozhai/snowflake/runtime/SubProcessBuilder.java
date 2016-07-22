package com.boluozhai.snowflake.runtime;

import com.boluozhai.snowflake.context.SnowContext;

public interface SubProcessBuilder extends SubProcessInfo {

	void setContext(SnowContext context);

	void setCommand(String cmd);

	void setCommand(String[] cmd);

	void setOutputHandler(LineHandler h);

	void setErrorHandler(ErrorHandler h);

	SubProcess create();

}
