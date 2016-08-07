package com.boluozhai.snowflake.xgit.vfs.scanner;

import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.xgit.vfs.FileWorkspace;

public interface FileScanner {

	VPath getBasePath();

	FileWorkspace getWorkspace();

	ScanningNode getBaseNode();

	UserDataFactory getUserDataFactory();

}
