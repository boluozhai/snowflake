package com.boluozhai.snowflake.libwebapp.manager;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface WebAppManagerFactory {

	WebAppManager getManager(SnowflakeContext context);

}
