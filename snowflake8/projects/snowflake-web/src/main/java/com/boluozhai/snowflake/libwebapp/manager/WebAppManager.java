package com.boluozhai.snowflake.libwebapp.manager;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.libwebapp.pojo.WebappInfo;

public interface WebAppManager {

	class Factory {

		public static WebAppManagerFactory getFactory(SnowflakeContext context) {
			String key = WebAppManagerFactory.class.getName();
			return context.getBean(key, WebAppManagerFactory.class);
		}

		public static WebAppManager getManager(SnowflakeContext context) {
			WebAppManagerFactory factory = getFactory(context);
			return factory.getManager(context);
		}

	}

	WebappInfo[] loadInstalledWebapps();

	WebappInfo[] getInstalledWebapps();

}
