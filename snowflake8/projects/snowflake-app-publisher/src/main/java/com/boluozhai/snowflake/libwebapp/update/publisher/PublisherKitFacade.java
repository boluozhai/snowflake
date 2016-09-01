package com.boluozhai.snowflake.libwebapp.update.publisher;

import java.io.File;
import java.net.URI;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.vfs.FileRepository;

public class PublisherKitFacade implements PublisherKit {

	private final PublisherKit in;

	public PublisherKitFacade(PublisherKit inner) {
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

	public File getMavenM2dir() {
		return in.getMavenM2dir();
	}

	public File getAppTableJsonFile() {
		return in.getAppTableJsonFile();
	}

	public AppData getAppData() {
		return in.getAppData();
	}

}
