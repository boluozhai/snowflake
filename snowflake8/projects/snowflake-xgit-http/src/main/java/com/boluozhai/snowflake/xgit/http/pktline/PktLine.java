package com.boluozhai.snowflake.xgit.http.pktline;

public class PktLine {

	/******
	 * pkt-len = 4 + pkt-payload-len
	 * */

	private int length;
	private PktPayload payload;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public PktPayload getPayload() {
		return payload;
	}

	public void setPayload(PktPayload payload) {
		this.payload = payload;
	}

}
