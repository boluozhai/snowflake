package com.boluozhai.snowflake.vfs.support;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFSFactory;
import com.boluozhai.snowflake.vfs.impl.VFSFactoryImpl;

public class DefaultVFSFactory implements VFSFactory {

	private final VFSFactory impl = new VFSFactoryImpl();

	@Override
	public VFS getVFS(SnowflakeContext app) {
		return impl.getVFS(app);
	}

}
