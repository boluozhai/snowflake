package com.boluozhai.snowflake.runtime;

public interface SubProcess extends SubProcessInfo {

	Runnable getRunnable();

	void start();

	void join() throws InterruptedException;

	void cancel();

	void kill();

	boolean done();

}
