package com.boluozhai.snowflake.pojo.bridge;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.pojo.bridge.merge.Merge;

public class PojoBridge implements Bridge {

	private final Bridge inner;

	public PojoBridge(Bridge in) {
		this.inner = in;
	}

	public PojoBridge(SnowflakeContext context) {
		BridgeFactory factory = BridgeFactory.getFactory(context);
		this.inner = factory.create(context, null);
	}

	public PojoBridge(SnowflakeContext context, String mappingConfigName) {
		BridgeFactory factory = BridgeFactory.getFactory(context);
		this.inner = factory.create(context, mappingConfigName);
	}

	@Override
	public BridgeChannel from(Object src) {
		return inner.from(src);
	}

	@Override
	public void setMerge(Merge merge) {
		inner.setMerge(merge);
	}

}
