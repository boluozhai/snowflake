package com.boluozhai.snowflake.h2o.data.pojo.element;

public class RepoItem {

	private String name;
	private String description;
	private String location; // file:url

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

}
