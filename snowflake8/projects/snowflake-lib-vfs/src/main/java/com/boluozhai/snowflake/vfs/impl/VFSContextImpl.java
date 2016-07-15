package com.boluozhai.snowflake.vfs.impl;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.support.ContextWrapper;
import com.boluozhai.snowflake.vfs.VFSContext;

public class VFSContextImpl extends ContextWrapper implements VFSContext {

	public VFSContextImpl(SnowContext context) {
		super(context);
	}

}
