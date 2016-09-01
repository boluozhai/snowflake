package com.boluozhai.snowflake.discovery.support;

import com.boluozhai.snowflake.discovery.scheme.PrivateScheme;
import com.boluozhai.snowflake.discovery.scheme.PublicScheme;
import com.boluozhai.snowflake.discovery.udp.HubRuntime;
import com.boluozhai.snowflake.discovery.udp.PrivateHandler;
import com.boluozhai.snowflake.discovery.udp.PublicHandler;

public class DefaultHubHandler implements PublicHandler, PrivateHandler {

	@Override
	public void rx(HubRuntime hr, PublicScheme data) {
		// TODO Auto-generated method stub

		System.out.println(data);

	}

	@Override
	public void rx(HubRuntime hr, PrivateScheme data) {
		// TODO Auto-generated method stub

		System.out.println(data);

	}

}
