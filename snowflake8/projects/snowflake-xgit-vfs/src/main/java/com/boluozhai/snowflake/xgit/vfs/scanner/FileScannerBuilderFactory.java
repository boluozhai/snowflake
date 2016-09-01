package com.boluozhai.snowflake.xgit.vfs.scanner;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface FileScannerBuilderFactory {

	FileScannerBuilder newBuilder(SnowflakeContext context);

}
