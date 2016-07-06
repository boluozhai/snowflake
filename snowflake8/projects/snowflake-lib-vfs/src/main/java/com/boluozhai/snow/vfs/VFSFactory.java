package com.boluozhai.snow.vfs;

import com.boluozhai.snowflake.context.SnowContext;

public interface VFSFactory {

	VFS getVFS(SnowContext app);

}
