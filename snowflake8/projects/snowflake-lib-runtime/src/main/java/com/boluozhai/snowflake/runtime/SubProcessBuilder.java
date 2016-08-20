package com.boluozhai.snowflake.runtime;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface SubProcessBuilder extends SubProcessInfo {

	void setContext(SnowflakeContext context);

	void setCommand(String cmd);

	void setCommand(String[] cmd);

	void setOutputHandler(LineHandler h);

	void setErrorHandler(ErrorHandler h);

	SubProcess create();

}
