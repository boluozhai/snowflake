package com.boluozhai.snowflake.libwebapp.update.publisher;

import java.io.File;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.libwebapp.update.UpdateKit;

public interface PublisherKit extends UpdateKit {

	public static class Agent {

		public static PublisherKit getInstance(SnowContext context) {
			DefaultPublisherKit in = new DefaultPublisherKit(context);
			return new PublisherKitFacade(in);
		}

	}

	File getMavenM2dir();

	File getAppTableJsonFile();

}
