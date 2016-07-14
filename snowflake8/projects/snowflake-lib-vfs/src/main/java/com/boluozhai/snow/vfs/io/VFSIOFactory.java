package com.boluozhai.snow.vfs.io;

import com.boluozhai.snowflake.context.SnowContext;

public interface VFSIOFactory {

	VFSIO getVFSIO(SnowContext app);

}
