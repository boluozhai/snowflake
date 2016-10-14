package com.boluozhai.snowflake.pojo.bridge;

import com.boluozhai.snowflake.pojo.bridge.merge.Merge;

public interface BridgeChannel {

	BridgeChannel to(Object target);

	BridgeChannel fieldFrom(String filed);

	BridgeChannel fieldTo(String filed);

	BridgeChannel fieldFromAll();

	BridgeChannel fieldToAll();

	BridgeChannel fieldAll();

	BridgeChannel setMerge(Merge merge);

	/*****
	 * @return if nothing changed, return 0 ; otherwise return count of fields
	 *         changed.
	 * */

	int go();

}
