package com.boluozhai.snowflake.xgit.vfs.scanner;

public interface GitIgnoreItem {

	boolean hasMore();

	GitIgnoreItem next();

	String getName();

}
