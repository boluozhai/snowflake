package com.boluozhai.snowflake.appdata;

import java.io.File;

import com.boluozhai.snowflake.appdata.support.StaticAppDataAgent;
import com.boluozhai.snowflake.context.SnowContext;

public interface AppData {

	Class<?> getTargetClass();

	File getCodePath();

	File getPropertiesPath();

	File getDataBasePath();

	File getDataSchemaPath();

	Exception getError();

	public class Helper {

		public static AppData getInstance(SnowContext context, Class<?> target) {
			AppDataAgent agent = AppDataAgent.getAgent(context);
			return agent.getAppData(target);
		}

		public static AppData getInstance(Class<?> target) {
			AppDataAgent agent = new StaticAppDataAgent();
			return agent.getAppData(target);
		}

	}

}
