package com.boluozhai.snowflake.libwebapp.update.impl;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.libwebapp.update.UpdateUserKit;

public class DefaultUpdateUserKit extends DefaultUpdateKit implements
		UpdateUserKit {

	public DefaultUpdateUserKit(SnowflakeContext context) {
		super(context);
	}

}
