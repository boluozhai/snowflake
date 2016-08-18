package com.boluozhai.snowflake.xgit.vfs.scanner;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.vfs.FileWorkspace;

public interface FileScannerBuilder {

	SnowContext context();

	FileScanner create(VFile path);

	FileScanner create(FileWorkspace workspace);

	FileScanner create(FileWorkspace workspace, VFile path);

	UserDataFactory getUserDataFactory();

	void setUserDataFactory(UserDataFactory factory);

}
