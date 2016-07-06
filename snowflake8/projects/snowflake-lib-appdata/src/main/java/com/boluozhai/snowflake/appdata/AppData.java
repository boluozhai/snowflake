package com.boluozhai.snowflake.appdata;

import java.io.File;

import com.boluozhai.snowflake.appdata.support.StaticAppDataAgent;
import com.boluozhai.snowflake.context.SnowContext;

public interface AppData {

	File getCodePath();

	File getPropertiesPath();

	File getDataBasePath();

	File getDataPath(Class<?> clazz);

	Exception getError();

	public class Helper {

		public static AppData getInstance(SnowContext context) {
			return AppDataAgent.getAppData(context);
		}

		public static AppData getInstance() {
			StaticAppDataAgent agent = new StaticAppDataAgent();
			return agent.getAppData();
		}

	}

}
