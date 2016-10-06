package com.boluozhai.snowflake.installer.min.pojo;

public class PackageDescriptor {

	private String name;
	private String url;
	private String description;
	private String sha1sum;
	private String version;
	private int size;
	private long timestamp;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSha1sum() {
		return sha1sum;
	}

	public void setSha1sum(String sha1sum) {
		this.sha1sum = sha1sum;
	}

}
