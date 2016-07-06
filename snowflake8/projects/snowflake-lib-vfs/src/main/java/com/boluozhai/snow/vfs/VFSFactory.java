package com.boluozhai.snow.vfs;

import com.boluozhai.snow.context.SnowContext;

public interface VFSFactory {

	VFS getVFS(SnowContext app);

}
