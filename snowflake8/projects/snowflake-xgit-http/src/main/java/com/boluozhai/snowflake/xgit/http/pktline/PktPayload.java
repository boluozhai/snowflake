package com.boluozhai.snowflake.xgit.http.pktline;

public class PktPayload {

	private byte[] data;
	private int offset;
	private int length;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void set(byte[] data, int off, int len) {
		this.data = data;
		this.offset = off;
		this.length = len;
	}

	public void set(byte[] data) {
		this.data = data;
		this.offset = 0;
		this.length = data.length;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
