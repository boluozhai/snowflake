package com.boluozhai.snow.vfs.support;

import com.boluozhai.snow.vfs.impl.VFSIOImpl;
import com.boluozhai.snow.vfs.io.VFSIO;
import com.boluozhai.snow.vfs.io.VFSIOFactory;
import com.boluozhai.snowflake.context.SnowContext;

public class DefaultVFSIOFactory implements VFSIOFactory {

	@Override
	public VFSIO getVFSIO(SnowContext context) {
		return new VFSIOImpl(context);
	}

}
