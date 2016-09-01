package com.boluozhai.snowflake.vfs;

public interface VPath {

	VFile file();

	VPath parent();

	VPath child(String name);

}
