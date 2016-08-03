package com.boluozhai.snowflake.cli.client;

public class ExecuteOption {

	private boolean createNewThread;
	private boolean startImmediately;

	public boolean isCreateNewThread() {
		return createNewThread;
	}

	public void setCreateNewThread(boolean createNewThread) {
		this.createNewThread = createNewThread;
	}

	public boolean isStartImmediately() {
		return startImmediately;
	}

	public void setStartImmediately(boolean startImmediately) {
		this.startImmediately = startImmediately;
	}

}
