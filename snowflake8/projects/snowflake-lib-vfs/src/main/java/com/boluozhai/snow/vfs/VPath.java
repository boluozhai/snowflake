package com.boluozhai.snow.vfs;

public interface VPath {

	VFile file();

	VPath parent();

	VPath child(String name);

}
