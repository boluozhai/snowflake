package com.boluozhai.snowflake.datatable;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.datatable.mapping.TypeMappingConfig;

public class DataClientConfiguration implements DataClientConfigurationAPI {

	private DataTableDriver driver;
	private DataSource dataSource;
	private TypeMappingConfig types;

	public DataClientConfiguration() {
	}

	public DataTableDriver getDriver() {
		return driver;
	}

	public void setDriver(DataTableDriver driver) {
		this.driver = driver;
	}

	public TypeMappingConfig getTypes() {
		return types;
	}

	public void setTypes(TypeMappingConfig types) {
		this.types = types;
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
