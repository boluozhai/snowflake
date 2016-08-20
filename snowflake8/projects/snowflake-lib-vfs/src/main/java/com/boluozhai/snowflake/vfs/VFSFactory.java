package com.boluozhai.snowflake.vfs;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface VFSFactory {

	VFS getVFS(SnowflakeContext app);

}
