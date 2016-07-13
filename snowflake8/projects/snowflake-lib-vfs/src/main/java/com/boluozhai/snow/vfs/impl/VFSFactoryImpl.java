package com.boluozhai.snow.vfs.impl;

import com.boluozhai.snow.vfs.VFS;
import com.boluozhai.snow.vfs.VFSFactory;
import com.boluozhai.snowflake.context.SnowContext;

public class VFSFactoryImpl implements VFSFactory {

	@Override
	public VFS getVFS(SnowContext app) {
		return new VFSImpl(app);
	}

}
