package com.boluozhai.snowflake.xgit.support;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.xgit.XGitRuntimeException;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryDriver;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;

public class DefaultRepositoryManager implements RepositoryManager {

	private Map<String, RepositoryDriver> _driver_table;
	private Map<String, RepositoryDriver> drivers;

	public Map<String, RepositoryDriver> getDrivers() {
		return drivers;
	}

	public void setDrivers(Map<String, RepositoryDriver> drivers) {
		this.drivers = drivers;
	}

	private Map<String, RepositoryDriver> inner_get_drivers() {
		Map<String, RepositoryDriver> map = this._driver_table;
		if (map == null) {
			map = Collections.synchronizedMap(this.drivers);
			this._driver_table = map;
		}
		return map;
	}

	@Override
	public Repository open(SnowContext context, URI uri, RepositoryOption option) {
		RepositoryDriver driver = this.getDriver(context, uri, option);
		if (driver == null) {
			String msg = "cannot find XGitDriver driver for the uri: " + uri;
			throw new XGitRuntimeException(msg);
		}
		return driver.open(context, uri, option);
	}

	@Override
	public RepositoryDriver getDriver(SnowContext context, URI uri,
			RepositoryOption option) {
		Map<String, RepositoryDriver> map = this.inner_get_drivers();
		String key = uri.getScheme();
		return map.get(key);
	}

}
