package com.boluozhai.snowflake.xgit.refs;

import com.boluozhai.snowflake.xgit.ObjectId;

public interface Reference {

	String getName();

	boolean isId();

	boolean isReferenceName();

	boolean exists();

	void setTargetId(ObjectId id);

	void setTargetReferenceName(String ref_name);

	ObjectId getTargetId();

	ObjectId loadTargetId();

	String getTargetReferenceName();

}
