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

	GitIgnore getIgnore();

	int FLAG_IGNORED = 0x80;
	int FLAG_IN_REPOSITORY = 0x40;
	int FLAG_IN_WORKSPACE = 0x20;

	/****
	 * @return [FLAG_IGNORE|FLAG_IN_REPOSITORY|FLAG_IN_WORKSPACE]
	 * */

	int flags();

	boolean isIgnored();

	boolean isInRepository();

	boolean isInWorkspace();

}
