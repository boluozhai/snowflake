package com.boluozhai.snow.discovery;

import java.io.IOException;

import org.junit.Test;

public class TestDiscovery {

	@Test
	public void test() {

		try {
			DiscoveryService ds = DiscoveryService.Factory.getService();
			DiscoveryHub hub = ds.open(0);

			hub.start();

			DiscoveryConnection conn = hub.connect();

			DiscoveryPacket packet = new DiscoveryPacket();
			conn.send(packet);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
