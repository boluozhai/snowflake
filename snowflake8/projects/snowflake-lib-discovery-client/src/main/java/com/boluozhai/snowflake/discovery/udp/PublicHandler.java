package com.boluozhai.snowflake.discovery.udp;

import com.boluozhai.snowflake.discovery.scheme.PublicScheme;

public interface PublicHandler {

	void rx(HubRuntime hr, PublicScheme data);

}
