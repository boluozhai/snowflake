package com.boluozhai.snowflake.xgit.vfs.impl.scanner;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScannerBuilder;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScannerBuilderFactory;

public class DefaultFileScannerBuilderFactory implements
		FileScannerBuilderFactory {

	@Override
	public FileScannerBuilder newBuilder(SnowflakeContext context) {
		return new InnerFileScannerBuilder(context);
	}

}
