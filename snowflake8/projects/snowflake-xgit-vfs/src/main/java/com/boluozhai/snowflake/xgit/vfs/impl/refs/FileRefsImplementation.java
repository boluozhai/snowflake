package com.boluozhai.snowflake.xgit.vfs.impl.refs;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;

public class FileRefsImplementation {

	public static ComponentBuilder newRefsBuilder() {
		return FileReferManagerImpl.newBuilder();
	}

	public static ComponentBuilder newRefptrsBuilder() {
		return FileRefptrManagerImpl.newBuilder();
	}

	public static ComponentBuilder newHrefsBuilder() {
		return FileHreferManagerImpl.newBuilder();
	}

}
