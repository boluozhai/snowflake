package com.boluozhai.snowflake.rest.element.repository;

import com.boluozhai.snowflake.rest.element.GitObjectDescriptor;

public class RepositoryProfile {

	private String name; // the repo id
	private String owner; // the uid of owner
	private String description;
	private GitObjectDescriptor icon;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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
