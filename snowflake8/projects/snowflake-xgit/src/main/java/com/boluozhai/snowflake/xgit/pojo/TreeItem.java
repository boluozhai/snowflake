package com.boluozhai.snowflake.xgit.pojo;

public class TreeItem {

	public interface MODE {

		int normal = 0x100644;
		int executable = 0x100755;
		int directory = 0x40000;

	}

	private int mode; // in hex
	private PlainId id;
	private String name;

	// extension fields

	private long size;
	private long lastModified;
	private boolean conflicted;

	public TreeItem() {
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

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

	public boolean isConflicted() {
		return conflicted;
	}

	public void setConflicted(boolean conflicted) {
		this.conflicted = conflicted;
	}

}
