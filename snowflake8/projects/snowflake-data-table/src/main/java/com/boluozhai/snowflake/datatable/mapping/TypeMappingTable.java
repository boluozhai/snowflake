package com.boluozhai.snowflake.datatable.mapping;

import com.boluozhai.snowflake.datatable.pojo.Model;

public interface TypeMappingTable {

	String[] listNames();

	TypeMappingInfo get(String name);

	TypeMappingInfo get(Class<? extends Model> type);

}
