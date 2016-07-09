package com.boluozhai.snow.discovery.scheme;

public class PublicSchemePojo {

	protected String scheme;

	protected long timestamp;
	protected int timeout;
	protected boolean broadcase;
	protected boolean response;

	protected String from_name;
	protected String from_host;
	protected int from_port;

	protected String to_name;
	protected String to_host;
	protected int to_port;

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean isBroadcase() {
		return broadcase;
	}

	public void setBroadcase(boolean broadcase) {
		this.broadcase = broadcase;
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}

	public String getFrom_name() {
		return from_name;
	}

	public void setFrom_name(String from_name) {
		this.from_name = from_name;
	}

	public String getFrom_host() {
		return from_host;
	}

	public void setFrom_host(String from_host) {
		this.from_host = from_host;
	}

	public int getFrom_port() {
		return from_port;
	}

	public void setFrom_port(int from_port) {
		this.from_port = from_port;
	}

	public String getTo_name() {
		return to_name;
	}

	public void setTo_name(String to_name) {
		this.to_name = to_name;
	}

	public String getTo_host() {
		return to_host;
	}

	public void setTo_host(String to_host) {
		this.to_host = to_host;
	}

	public int getTo_port() {
		return to_port;
	}

	public void setTo_port(int to_port) {
		this.to_port = to_port;
	}

}
