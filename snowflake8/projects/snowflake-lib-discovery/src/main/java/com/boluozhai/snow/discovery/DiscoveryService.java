package com.boluozhai.snow.discovery;

import java.io.IOException;

import com.boluozhai.snow.discovery.support.DefaultConnector;

public interface DiscoveryService {

	/***
	 * @param port
	 *            if less than 100, set to default port , default is 10217
	 * */

	public abstract DiscoveryHub open(int port) throws IOException;

	public static class Factory {

		public static DiscoveryService getService() {
			return new DefaultConnector();
		}

	}

}
