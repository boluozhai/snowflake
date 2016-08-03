package com.boluozhai.snowflake.libwebapp.manager;

import com.boluozhai.snowflake.context.SnowContext;

public interface WebAppManagerFactory {

	WebAppManager getManager(SnowContext context);

}
