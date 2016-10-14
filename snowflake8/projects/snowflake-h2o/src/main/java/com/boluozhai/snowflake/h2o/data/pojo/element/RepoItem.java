package com.boluozhai.snowflake.h2o.data.pojo.element;

import com.boluozhai.snowflake.rest.element.GitObjectDescriptor;

public class RepoItem {

	private String name;
	private String description;
	private String descriptor;
	private String location; // file:url
	private String ownerUid;
	private GitObjectDescriptor icon;

	public String getOwnerUid() {
		return ownerUid;
	}

	public void setOwnerUid(String ownerUid) {
		this.ownerUid = ownerUid;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public GitObjectDescriptor getIcon() {
		return icon;
	}

	public void setIcon(GitObjectDescriptor icon) {
		this.icon = icon;
	}

}
