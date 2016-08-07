package com.boluozhai.snowflake.xgit.vfs.scanner;

import com.boluozhai.snowflake.xgit.vfs.impl.scanner.DefaultFileScannerBuilderFactory;

public final class FileScannerUtils {

	public static FileScannerBuilderFactory getFactory() {
		return new DefaultFileScannerBuilderFactory();
	}

}
