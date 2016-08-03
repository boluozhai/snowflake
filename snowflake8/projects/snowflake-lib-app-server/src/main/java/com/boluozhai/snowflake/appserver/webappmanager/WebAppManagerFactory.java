package com.boluozhai.snowflake.appserver.webappmanager;

import com.boluozhai.snowflake.context.SnowContext;

public interface WebAppManagerFactory {

	WebAppManager getManager(SnowContext context);

}
