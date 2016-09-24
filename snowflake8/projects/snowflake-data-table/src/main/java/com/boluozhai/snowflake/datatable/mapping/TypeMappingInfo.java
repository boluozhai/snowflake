package com.boluozhai.snowflake.datatable.mapping;

import com.boluozhai.snowflake.datatable.pojo.Model;

public interface TypeMappingInfo {

	Class<? extends Model> type();

	String name();

}
