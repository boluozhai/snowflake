package com.boluozhai.snow.discovery.impl;

import java.io.IOException;

import com.boluozhai.snow.discovery.DiscoveryHub;
import com.boluozhai.snow.discovery.DiscoveryService;

public class ConnectorImpl implements DiscoveryService {

	@Override
	public DiscoveryHub open(int port) throws IOException {
		if (port < 100) {
			port = 10217;
		}
		return DiscoveryHubImpl.open(port);
	}

}
