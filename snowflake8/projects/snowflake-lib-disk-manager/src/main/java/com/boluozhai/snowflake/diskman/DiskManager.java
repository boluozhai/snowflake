package com.boluozhai.snowflake.diskman;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.diskman.model.FsTable;

public interface DiskManager {

	class Agent {

		public static DiskManager getManager(SnowContext context) {
			DiskManagerAgent agent = getAgent(context);
			return agent.getManager(context);
		}

		public static DiskManagerAgent getAgent(SnowContext context) {
			Class<DiskManagerAgent> type = DiskManagerAgent.class;
			String key = type.getName();
			return context.getBean(key, type);
		}

	}

	FsTable getFsTable();

	FsTable loadFsTable();

}
