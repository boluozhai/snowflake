package com.boluozhai.snowflake.rest.element.account;

import com.boluozhai.snowflake.rest.element.GitObjectDescriptor;

public class AccountProfile {

	private String hashId;
	private String uid;
	private String nickname;
	private String email;
	private String description;
	private String language;
	private String location;
	private boolean exists;
	private GitObjectDescriptor avatar;

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHashId() {
		return hashId;
	}

	public void setHashId(String hashId) {
		this.hashId = hashId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public GitObjectDescriptor getAvatar() {
		return avatar;
	}

	public void setAvatar(GitObjectDescriptor avatar) {
		this.avatar = avatar;
	}

}
