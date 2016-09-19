package com.boluozhai.snowflake.datatable;

import java.util.Map;

import com.boluozhai.snowflake.context.SnowflakeContext;

public class DataClientConfiguration implements DataClientConfigurationAPI {

	private DataTableDriver driver;
	private Map<String, TableMeta> tables;
	private DataSource dataSource;

	public DataClientConfiguration() {
	}

	public DataTableDriver getDriver() {
		return driver;
	}

	public void setDriver(DataTableDriver driver) {
		this.driver = driver;
	}

	public Map<String, TableMeta> getTables() {
		return tables;
	}

	public void setTables(Map<String, TableMeta> tables) {
		this.tables = tables;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public DataClientFactory configure(SnowflakeContext context) {
		return this.driver.createFactory(context, this);
	}

}
