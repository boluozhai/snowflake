package com.boluozhai.snowflake.xgit.site.pojo;

public class SiteRepoInfo {

	private String id;
	private String uri; // a 'file:' URI
	private String type; // {system|partition|data|user}
	private String descriptor; // a hash-id to the descriptor

	public SiteRepoInfo() {
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
