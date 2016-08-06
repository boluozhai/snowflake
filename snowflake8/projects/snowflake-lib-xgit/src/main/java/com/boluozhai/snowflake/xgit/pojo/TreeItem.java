package com.boluozhai.snowflake.xgit.pojo;

public class TreeItem {

	public interface MODE {

		int normal = 0x100644;
		int executable = 0x100755;
		int directory = 0x40000;

	}

	private String name;
	private int mode; // in hex
	private PlainId id;

	public PlainId getId() {
		return id;
	}

	public void setId(PlainId id) {
		this.id = id;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
