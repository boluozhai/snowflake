package com.boluozhai.snowflake.xgit.objects;

import com.boluozhai.snowflake.xgit.ObjectId;

public interface GitObject {

	ObjectBank owner();

	ObjectId id();

	String type();

	long length();

	long zippedSize();

	GitObjectEntity entity();

	boolean exists();

	void delete();

}
