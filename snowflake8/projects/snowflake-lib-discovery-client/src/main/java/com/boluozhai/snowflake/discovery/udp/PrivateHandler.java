package com.boluozhai.snowflake.discovery.udp;

import com.boluozhai.snowflake.discovery.scheme.PrivateScheme;

public interface PrivateHandler {

	void rx(HubRuntime hr, PrivateScheme data);

}
