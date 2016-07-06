package com.boluozhai.snow.context;

public interface SnowAttributes {

	String[] getAttributeNames();

	Object getAttribute(String name);

	Object getAttribute(String name, Object defaultValue);

}
