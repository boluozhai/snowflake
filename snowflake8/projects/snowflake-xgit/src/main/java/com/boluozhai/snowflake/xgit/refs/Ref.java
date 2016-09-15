package com.boluozhai.snowflake.xgit.refs;

import com.boluozhai.snowflake.xgit.ObjectId;

public interface Ref {

	String getName();

	boolean exists();

	boolean delete();

	void setId(ObjectId id);

	ObjectId getId();

	ObjectId getId(boolean reload);

}
