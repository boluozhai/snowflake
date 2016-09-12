package com.boluozhai.snowflake.xgit.site.pojo;

public class SitePartitionInfo {

	private String id;
	private String name;
	private String location; // a file URI
	private long size;

	public SitePartitionInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
