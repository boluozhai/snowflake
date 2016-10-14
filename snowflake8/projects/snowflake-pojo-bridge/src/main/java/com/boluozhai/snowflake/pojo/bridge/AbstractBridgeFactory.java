package com.boluozhai.snowflake.pojo.bridge;

import java.util.Map;

import com.boluozhai.snowflake.pojo.bridge.config.BridgeConfiguration;

public abstract class AbstractBridgeFactory extends BridgeFactory {

	private BridgeConfiguration defaultConfiguration;
	private Map<String, BridgeConfiguration> configurations;

	public BridgeConfiguration getDefaultConfiguration() {
		return defaultConfiguration;
	}

	public void setDefaultConfiguration(BridgeConfiguration defaultConfiguration) {
		this.defaultConfiguration = defaultConfiguration;
	}

	public Map<String, BridgeConfiguration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(
			Map<String, BridgeConfiguration> configurations) {
		this.configurations = configurations;
	}

}
