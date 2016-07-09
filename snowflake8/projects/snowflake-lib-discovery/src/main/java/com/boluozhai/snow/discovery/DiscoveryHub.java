package com.boluozhai.snow.discovery;

import java.io.IOException;

public interface DiscoveryHub {

	int getBufferSize();

	int getPort();

	void start();

	void stop();

	void join() throws InterruptedException;

	/***
	 * @param port
	 *            if less than 100, set to default port , default is 10217
	 * */

	public abstract DiscoveryConnection connect() throws IOException;

}
