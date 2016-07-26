package com.boluozhai.snowflake.xgit.objects;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitComponent;

public interface ObjectBank extends XGitComponent {

	GitObject object(ObjectId id);

	GitObjectBuilder newBuilder(String type, long length);

}
