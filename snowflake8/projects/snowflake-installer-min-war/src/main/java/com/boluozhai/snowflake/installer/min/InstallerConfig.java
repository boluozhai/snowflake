package com.boluozhai.snowflake.installer.min;

import com.boluozhai.snowflake.installer.min.context.ApplicationContext;
import com.boluozhai.snowflake.installer.min.pojo.SpringConfigBean;

public class InstallerConfig extends SpringConfigBean {

	public static InstallerConfig getInstance(ApplicationContext context) {
		Class<InstallerConfig> clazz = InstallerConfig.class;
		String key = clazz.getName();
		return context.getBean(key, clazz);
	}

}
