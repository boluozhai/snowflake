package com.boluozhai.snow.discovery;

import java.io.IOException;

import org.junit.Test;

import com.boluozhai.snow.discovery.scheme.PrivateScheme;

public class TestDiscovery {

	@Test
	public void test() {
		// this.inner_test();
	}

	private void inner_test() {

		DiscoveryHub hub = null;

		try {
			DiscoveryService ds = DiscoveryService.Factory.getService();
			hub = ds.open(0);

			hub.start();

			DiscoveryConnection conn = hub.connect();

			PrivateScheme packet = new PrivateScheme();
			conn.send(packet);
			this.log("Tx", packet);
			packet = conn.receive(packet);
			this.log("Rx", packet);

			hub.stop();
			hub.join();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (InterruptedException e) {
			e.printStackTrace();

		} finally {

		}

	}

	private void log(String tag, PrivateScheme packet) {

		// TODO Auto-generated method stub

		System.out.println(tag + " " + packet);

	}

}
