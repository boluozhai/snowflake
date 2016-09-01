package com.boluozhai.snowflake.vfs.impl;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.support.ContextWrapper;
import com.boluozhai.snowflake.vfs.VFSContext;

public class VFSContextImpl extends ContextWrapper implements VFSContext {

	public VFSContextImpl(SnowflakeContext context) {
		super(context);
	}

}
