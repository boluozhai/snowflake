package com.boluozhai.snowflake.xgit.vfs.scanner;

import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VFileNode;
import com.boluozhai.snowflake.vfs.VPath;

public interface ScanningNode extends VFileNode {

	String getName();

	FileScanner getScanner();

	VPath getPath();

	VFile getFile();

	Object getUserData();

	ScanningNode parent();

	ScanningNode child(String name);

	boolean isIgnored();

	GitIgnore getIgnore();

}
