package com.boluozhai.snowflake.vfs;

public interface VPath {

	String name();

	VFile file();

	VPath parent();

	VPath child(String name);

}
