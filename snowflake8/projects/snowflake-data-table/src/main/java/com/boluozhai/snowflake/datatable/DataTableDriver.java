package com.boluozhai.snowflake.datatable;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface DataTableDriver {

	DataClientFactory createFactory(SnowflakeContext context,
			DataClientConfiguration conf);

}
