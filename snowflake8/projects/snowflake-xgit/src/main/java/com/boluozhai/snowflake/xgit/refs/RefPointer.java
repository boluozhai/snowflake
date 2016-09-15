package com.boluozhai.snowflake.xgit.refs;

public interface RefPointer {

	String getName();

	boolean exists();

	boolean delete();

	String getRefname();

	String getRefname(boolean reload);

	void setRefname(String refname);

}
