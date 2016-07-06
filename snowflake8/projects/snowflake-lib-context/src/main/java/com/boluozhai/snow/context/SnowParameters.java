package com.boluozhai.snow.context;

public interface SnowParameters {

	String[] getParameterNames();

	String getParameter(String name);

	String getParameter(String name, Object defaultValue);

}
