package com.boluozhai.snowflake.xgit.vfs.impl.refs;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.xgit.vfs.support.FileRefsManagerFactory;

public class FileRefsImplementation {

	public static ComponentBuilder newRefsBuilder(FileRefsManagerFactory factory) {
		return FileReferManagerImpl.newBuilder( factory );
	}

	public static ComponentBuilder newRefptrsBuilder() {
		return FileRefptrManagerImpl.newBuilder();
	}

	public static ComponentBuilder newHrefsBuilder() {
		return FileHreferManagerImpl.newBuilder();
	}

}
