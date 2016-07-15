package com.boluozhai.snowflake.vfs;

import com.boluozhai.snowflake.context.SnowContext;

public interface VFSFactory {

	VFS getVFS(SnowContext app);

}
