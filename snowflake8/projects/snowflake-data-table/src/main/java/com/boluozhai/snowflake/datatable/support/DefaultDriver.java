package com.boluozhai.snowflake.datatable.support;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.datatable.DataClientConfiguration;
import com.boluozhai.snowflake.datatable.DataClientFactory;
import com.boluozhai.snowflake.datatable.DataTableDriver;
import com.boluozhai.snowflake.datatable.impl.DataTableImplementation;

public class DefaultDriver implements DataTableDriver {

	@Override
	public DataClientFactory createFactory(SnowflakeContext context,
			DataClientConfiguration conf) {
		DataTableDriver inner = DataTableImplementation.getDefaultDriverImpl();
		return inner.createFactory(context, conf);
	}

}
