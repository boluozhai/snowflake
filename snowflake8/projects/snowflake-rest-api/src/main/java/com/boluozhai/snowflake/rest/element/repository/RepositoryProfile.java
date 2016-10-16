package com.boluozhai.snowflake.rest.element.repository;

import com.boluozhai.snowflake.rest.element.GitObjectDescriptor;
import com.boluozhai.snowflake.rest.element.account.AccountProfile;

public class RepositoryProfile {

	private String name; // the repo id
	private String description;
	private String url;
	private boolean exists;
	private boolean theDefault;
	private AccountProfile owner;
	private GitObjectDescriptor icon;

	public boolean isTheDefault() {
		return theDefault;
	}

	public void setTheDefault(boolean theDefault) {
		this.theDefault = theDefault;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public AccountProfile getOwner() {
		return owner;
	}

	public void setOwner(AccountProfile owner) {
		this.owner = owner;
	}

	public GitObjectDescriptor getIcon() {
		return icon;
	}

	public void setIcon(GitObjectDescriptor icon) {
		this.icon = icon;
	}

}
