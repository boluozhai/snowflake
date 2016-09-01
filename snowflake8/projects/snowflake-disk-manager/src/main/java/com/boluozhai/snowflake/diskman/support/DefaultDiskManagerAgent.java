package com.boluozhai.snowflake.diskman.support;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.diskman.DiskManager;
import com.boluozhai.snowflake.diskman.DiskManagerAgent;
import com.boluozhai.snowflake.diskman.impl.DefaultDiskManager;

public class DefaultDiskManagerAgent implements DiskManagerAgent {

	@Override
	public DiskManager getManager(SnowflakeContext context) {
		return DefaultDiskManager.newInstance(context);
	}

}
