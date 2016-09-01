package com.boluozhai.snowflake.vfs.support;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.vfs.impl.VFSIOImpl;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.vfs.io.VFSIOFactory;

public class DefaultVFSIOFactory implements VFSIOFactory {

	@Override
	public VFSIO getVFSIO(SnowflakeContext context) {
		return new VFSIOImpl(context);
	}

}
