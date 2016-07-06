package com.boluozhai.snow.discovery;

import java.io.IOException;

public interface DiscoveryHub {

	int getPort();

	void start();

	void stop();

	/***
	 * @param port
	 *            if less than 100, set to default port , default is 10217
	 * */

	public abstract DiscoveryConnection connect() throws IOException;

}
