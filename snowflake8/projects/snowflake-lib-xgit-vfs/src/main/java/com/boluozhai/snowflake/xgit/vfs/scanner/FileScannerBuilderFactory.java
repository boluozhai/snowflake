package com.boluozhai.snowflake.xgit.vfs.scanner;

import com.boluozhai.snowflake.context.SnowContext;

public interface FileScannerBuilderFactory {

	FileScannerBuilder newBuilder(SnowContext context);

}
