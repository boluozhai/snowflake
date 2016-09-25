package com.boluozhai.snowflake.xgit.vfs.support;

import java.util.List;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.xgit.vfs.impl.refs.FileRefsImplementation;

public class FileRefsManagerFactory implements ComponentBuilderFactory {

	private List<String> acceptPrefix;

	@Override
	public ComponentBuilder newBuilder() {
		return FileRefsImplementation.newRefsBuilder(  this );
	}

	public List<String> getAcceptPrefix() {
		return acceptPrefix;
	}

	public void setAcceptPrefix(List<String> acceptPrefix) {
		this.acceptPrefix = acceptPrefix;
	}

}
