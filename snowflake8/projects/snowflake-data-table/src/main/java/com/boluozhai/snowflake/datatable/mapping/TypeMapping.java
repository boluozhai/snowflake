package com.boluozhai.snowflake.datatable.mapping;

public interface TypeMapping {

	String[] listNames();

	Class<?> getClass(String name);

	String getName(Class<?> type);

}
