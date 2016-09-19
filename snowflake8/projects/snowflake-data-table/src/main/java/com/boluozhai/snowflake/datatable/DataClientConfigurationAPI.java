package com.boluozhai.snowflake.datatable;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface DataClientConfigurationAPI {

	DataClientFactory configure(SnowflakeContext context);

}
