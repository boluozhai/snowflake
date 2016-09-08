package com.boluozhai.snowflake.xgit.site;

import java.net.URI;

public class RepositoryInfo {

	private String name;
	private String type = RepositoryType.normal;
	private String description;
	private URI uri;// the named id of this repository
	private URI path; // the path in disk

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public URI getPath() {
		return path;
	}

	public void setPath(URI path) {
		this.path = path;
	}

}
