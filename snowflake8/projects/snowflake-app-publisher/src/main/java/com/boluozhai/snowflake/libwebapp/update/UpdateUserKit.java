package com.boluozhai.snowflake.libwebapp.update;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.libwebapp.update.impl.DefaultUpdateUserKit;

public interface UpdateUserKit extends UpdateKit {

	public static class Agent {

		public static UpdateUserKit getInstance(SnowflakeContext context) {
			return new DefaultUpdateUserKit(context);
		}

	}

}
