package com.boluozhai.snowflake.xgit.vfs.scanner;

import com.boluozhai.snowflake.vfs.VPath;

public interface ScanningNode {

	String getName();

	FileScanner getScanner();

	VPath getPath();

	Object getUserData();

	ScanningNode child(String name);

}
