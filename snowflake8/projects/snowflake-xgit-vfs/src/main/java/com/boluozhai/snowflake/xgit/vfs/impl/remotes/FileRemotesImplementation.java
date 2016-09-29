package com.boluozhai.snowflake.xgit.vfs.impl.remotes;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.xgit.vfs.support.FileRemoteManagerFactory;

public final class FileRemotesImplementation {

	public static ComponentBuilder newRemotesBuilder(
			FileRemoteManagerFactory factory) {

		return new RemotesBuilderImpl(factory);
	}

}
