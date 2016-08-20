package com.boluozhai.snowflake.appdata;

import java.io.File;

import com.boluozhai.snowflake.appdata.support.StaticAppDataAgent;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.SnowflakeProperties;

public interface AppData extends SnowflakeProperties {

	Class<?> getTargetClass();

	File getCodePath();

	File getPropertiesPath();

	File getDataBasePath();

	File getDataSchemaPath();

	Exception getError();

	public class Helper {

		public static AppData getInstance(SnowflakeContext context, Class<?> target) {
			AppDataAgent agent = AppDataAgent.getAgent(context);
			return agent.getAppData(target);
		}

		public static AppData getInstance(Class<?> target) {
			AppDataAgent agent = new StaticAppDataAgent();
			return agent.getAppData(target);
		}

	}

}
