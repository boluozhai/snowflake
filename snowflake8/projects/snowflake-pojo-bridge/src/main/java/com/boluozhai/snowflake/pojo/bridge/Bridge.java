package com.boluozhai.snowflake.pojo.bridge;

import com.boluozhai.snowflake.pojo.bridge.merge.Merge;

public interface Bridge {

	void setMerge(Merge merge);

	BridgeChannel from(Object src);

}
