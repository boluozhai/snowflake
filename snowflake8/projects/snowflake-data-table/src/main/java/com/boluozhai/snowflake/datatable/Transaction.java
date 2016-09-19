package com.boluozhai.snowflake.datatable;

import com.boluozhai.snowflake.core.SnowflakeException;

public interface Transaction {

	void begin() throws SnowflakeException;

	void commit() throws SnowflakeException;

	void rollback() throws SnowflakeException;

}
