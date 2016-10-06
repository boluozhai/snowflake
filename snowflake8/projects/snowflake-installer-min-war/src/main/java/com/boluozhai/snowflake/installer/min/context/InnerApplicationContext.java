package com.boluozhai.snowflake.installer.min.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.installer.min.InstallerConfig;
import com.boluozhai.snowflake.installer.min.pojo.PackageDescriptor;

final class InnerApplicationContext implements ApplicationContext {

	private Map<String, Object> beans;

	public InnerApplicationContext() {
		this.beans = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String key, Class<T> clazz) {
		Object obj = beans.get(key);
		if (obj == null) {
			throw new SnowflakeException("need bean: " + key);
		}
		return (T) obj;
	}

	public void init() {
		Map<String, Object> map = new HashMap<String, Object>();
		MyBeanLoader loader = new MyBeanLoader();
		loader.load_config(map);
		this.beans = Collections.synchronizedMap(map);
	}

	private class MyBeanLoader {

		public void load_config(Map<String, Object> map) {

			PackageDescriptor remote = new PackageDescriptor();
			remote.setUrl("https://www.boluozhai.com/download/snowflake-installer/info.json");
			remote.setName("snowflake-installer");

			InstallerConfig conf = new InstallerConfig();
			conf.setHttpsOnly(true);
			conf.setRemote(remote);

			String key = conf.getClass().getName();
			map.put(key, conf);

		}
	}

}
