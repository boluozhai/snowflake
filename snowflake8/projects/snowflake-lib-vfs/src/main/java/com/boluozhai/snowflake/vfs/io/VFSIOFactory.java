package com.boluozhai.snowflake.vfs.io;

import com.boluozhai.snowflake.context.SnowContext;

public interface VFSIOFactory {

	VFSIO getVFSIO(SnowContext app);

}
