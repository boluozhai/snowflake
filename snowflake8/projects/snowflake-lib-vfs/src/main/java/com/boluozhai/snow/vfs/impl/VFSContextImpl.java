package com.boluozhai.snow.vfs.impl;

import com.boluozhai.snow.vfs.VFSContext;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.support.ContextWrapper;

public class VFSContextImpl extends ContextWrapper implements VFSContext {

	public VFSContextImpl(SnowContext context) {
		super(context);
	}

}
