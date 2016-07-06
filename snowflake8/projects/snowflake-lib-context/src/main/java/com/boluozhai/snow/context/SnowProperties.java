package com.boluozhai.snow.context;

public interface SnowProperties {

	String[] getPropertyNames();

	String getProperty(String name);

	String getProperty(String name, Object defaultValue);

}
