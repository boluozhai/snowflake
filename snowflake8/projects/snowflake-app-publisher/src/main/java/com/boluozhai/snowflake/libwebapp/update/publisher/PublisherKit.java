package com.boluozhai.snowflake.libwebapp.update.publisher;

import java.io.File;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.libwebapp.update.UpdateKit;

public interface PublisherKit extends UpdateKit {

	public static class Agent {

		public static PublisherKit getInstance(SnowflakeContext context) {
			DefaultPublisherKit in = new DefaultPublisherKit(context);
			return new PublisherKitFacade(in);
		}

	}

	File getMavenM2dir();

	File getAppTableJsonFile();

}
