package com.boluozhai.snowflake.xgit.vfs.scanner;

import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;

public interface ScanningNode {

	String getName();

	FileScanner getScanner();

	VPath getPath();

	VFile getFile();

	Object getUserData();

	ScanningNode child(String name);

}
