package com.boluozhai.snowflake.discovery.udp;

public interface HubRuntime extends Runnable, PublicDispatcher,
		PrivateDispatcher {

	void start();

	void stop();

	void join() throws InterruptedException;

	boolean started();

	boolean stopped();

	Hub getHub();

}
