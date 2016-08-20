package com.boluozhai.snowflake.diskman;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.diskman.model.FsTable;

public interface DiskManager {

	class Agent {

		public static DiskManager getManager(SnowflakeContext context) {
			DiskManagerAgent agent = getAgent(context);
			return agent.getManager(context);
		}

		public static DiskManagerAgent getAgent(SnowflakeContext context) {
			Class<DiskManagerAgent> type = DiskManagerAgent.class;
			String key = type.getName();
			return context.getBean(key, type);
		}

	}

	FsTable getFsTable();

	FsTable loadFsTable();

}
