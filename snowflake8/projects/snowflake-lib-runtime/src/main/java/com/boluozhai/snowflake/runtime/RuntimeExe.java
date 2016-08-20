package com.boluozhai.snowflake.runtime;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface RuntimeExe {

	SubProcessBuilder newSubProcessBuilder();

	class Agent {

		public static RuntimeExe getInstance(SnowflakeContext context) {
			Class<RuntimeExe> type = RuntimeExe.class;
			String key = type.getName();
			return context.getBean(key, type);
		}

	}

}
