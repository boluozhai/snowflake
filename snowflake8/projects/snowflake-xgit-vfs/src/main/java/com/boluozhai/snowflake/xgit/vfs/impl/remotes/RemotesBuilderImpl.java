package com.boluozhai.snowflake.xgit.vfs.impl.remotes;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.vfs.FileRemotes;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.vfs.support.FileRemoteManagerFactory;

final class RemotesBuilderImpl extends FileXGitComponentBuilder {

	private final FileRemoteManagerFactory factory;

	public RemotesBuilderImpl(FileRemoteManagerFactory factory) {
		this.factory = factory;
	}

	@Override
	public void configure(ContextBuilder cb) {
		this.factory.hashCode();
	}

	@Override
	public Component create(ComponentContext cc) {
		FileRemotes fr = new RemotesImpl(this, cc);
		return fr;
	}

}
