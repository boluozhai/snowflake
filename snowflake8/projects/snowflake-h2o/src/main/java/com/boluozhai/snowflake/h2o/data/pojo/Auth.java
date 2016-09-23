package com.boluozhai.snowflake.h2o.data.pojo;

import com.boluozhai.snowflake.datatable.pojo.ForeignKey;

public class Auth {

	private String id;
	private String type;
	private String name;
	private String key;
	private long fromTime;
	private long toTime;
	private ForeignKey account;

	public Auth() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ForeignKey getAccount() {
		return account;
	}

	public void setAccount(ForeignKey account) {
		this.account = account;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getFromTime() {
		return fromTime;
	}

	public void setFromTime(long fromTime) {
		this.fromTime = fromTime;
	}

	public long getToTime() {
		return toTime;
	}

	public void setToTime(long toTime) {
		this.toTime = toTime;
	}

}
