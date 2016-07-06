package com.boluozhai.snow.discovery;

public class DiscoveryPacket {

	private int timeout;
	private DiscoveryEndpoint from;
	private DiscoveryEndpoint to;
	private boolean broadcast;
	private boolean request;
	private boolean response;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public DiscoveryEndpoint getFrom() {
		return from;
	}

	public void setFrom(DiscoveryEndpoint from) {
		this.from = from;
	}

	public DiscoveryEndpoint getTo() {
		return to;
	}

	public void setTo(DiscoveryEndpoint to) {
		this.to = to;
	}

	public boolean isBroadcast() {
		return broadcast;
	}

	public void setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
	}

	public boolean isRequest() {
		return request;
	}

	public void setRequest(boolean request) {
		this.request = request;
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}

}
