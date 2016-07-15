package com.boluozhai.snowflake.vfs.impl;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFSFactory;

public class VFSFactoryImpl implements VFSFactory {

	@Override
	public VFS getVFS(SnowContext app) {
		return new VFSImpl(app);
	}

}
