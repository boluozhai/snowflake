package com.boluozhai.snow.vfs.support;

import com.boluozhai.snow.vfs.VFS;
import com.boluozhai.snow.vfs.VFSFactory;
import com.boluozhai.snow.vfs.impl.VFSFactoryImpl;
import com.boluozhai.snowflake.context.SnowContext;

public class DefaultVFSFactory implements VFSFactory {

	private final VFSFactory impl = new VFSFactoryImpl();

	@Override
	public VFS getVFS(SnowContext app) {
		return impl.getVFS(app);
	}

}
