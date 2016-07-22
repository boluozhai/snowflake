package com.boluozhai.snowflake.diskman;

import com.boluozhai.snowflake.context.SnowContext;

public interface DiskManagerAgent {

	DiskManager getManager(SnowContext context);

}
