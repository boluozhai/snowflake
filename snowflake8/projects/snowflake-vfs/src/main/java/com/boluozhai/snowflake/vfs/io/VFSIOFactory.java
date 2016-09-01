package com.boluozhai.snowflake.vfs.io;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface VFSIOFactory {

	VFSIO getVFSIO(SnowflakeContext app);

}
