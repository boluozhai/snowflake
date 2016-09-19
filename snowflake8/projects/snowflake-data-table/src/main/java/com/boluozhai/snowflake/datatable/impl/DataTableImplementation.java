package com.boluozhai.snowflake.datatable.impl;

import com.boluozhai.snowflake.datatable.DataTableDriver;

public class DataTableImplementation {

	public static DataTableDriver getDefaultDriverImpl() {
		return new DefaultDriverImpl();
	}

}
