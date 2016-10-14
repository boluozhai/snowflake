package com.boluozhai.snowflake.pojo.bridge.config;

import java.util.List;

public class BridgeConfiguration {

	private List<BridgeMapping> mappings;

	public List<BridgeMapping> getMappings() {
		return mappings;
	}

	public void setMappings(List<BridgeMapping> mappings) {
		this.mappings = mappings;
	}

}
