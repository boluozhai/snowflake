package com.boluozhai.snowflake.installer.min.context;

import javax.servlet.ServletContext;

public class WebApplicationContextUtils {

	private static final InnerApplicationContextFactory factory;

	static {
		factory = new InnerApplicationContextFactory();
	}

	public static ApplicationContext getRequiredWebApplicationContext(
			ServletContext sc) {

		return factory.getAC(sc);

	}
}
