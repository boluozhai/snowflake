package com.boluozhai.snowflake.libwebapp.update;

import java.net.URI;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.vfs.FileRepository;

public class UpdateKitFacade implements UpdateKit {

	private final UpdateKit in;

	public UpdateKitFacade(UpdateKit inner) {
		this.in = inner;
	}

	public SnowflakeContext getContext() {
		return in.getContext();
	}

	public VFile getWebappsDir() {
		return in.getWebappsDir();
	}

	public FileRepository getRepository() {
		return in.getRepository();
	}

	public URI getRemoteURI() {
		return in.getRemoteURI();
	}

	public String getUserGroup() {
		return in.getUserGroup();
	}

	public AppData getAppData() {
		return in.getAppData();
	}

}
