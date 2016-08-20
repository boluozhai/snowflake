package com.boluozhai.snowflake.diskman;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface DiskManagerAgent {

	DiskManager getManager(SnowflakeContext context);

}
