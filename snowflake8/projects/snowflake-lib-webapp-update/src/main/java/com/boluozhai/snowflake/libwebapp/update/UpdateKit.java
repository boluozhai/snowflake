package com.boluozhai.snowflake.libwebapp.update;

import java.net.URI;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.libwebapp.update.impl.DefaultUpdateKit;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.vfs.FileRepository;

public interface UpdateKit {

	SnowContext getContext();

	AppData getAppData();

	VFile getWebappsDir();

	FileRepository getRepository();

	URI getRemoteURI();

	String getUserGroup();

	public static class Agent {

		public static UpdateKit getInstance(SnowContext context) {
			DefaultUpdateKit in = new DefaultUpdateKit(context);
			return new UpdateKitFacade(in);
		}

	}

}
