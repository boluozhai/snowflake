package com.boluozhai.snowflake.appserver.webappmanager;

import com.boluozhai.snowflake.appserver.pojo.WebappInfo;
import com.boluozhai.snowflake.context.SnowContext;

public interface WebAppManager {

	class Factory {

		public static WebAppManagerFactory getFactory(SnowContext context) {
			String key = WebAppManagerFactory.class.getName();
			return context.getBean(key, WebAppManagerFactory.class);
		}

		public static WebAppManager getManager(SnowContext context) {
			WebAppManagerFactory factory = getFactory(context);
			return factory.getManager(context);
		}

	}

	WebappInfo[] loadInstalledWebapps();

	WebappInfo[] getInstalledWebapps();

}
