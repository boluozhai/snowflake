package com.boluozhai.snowflake.h2o.data.pojo.model;

import com.boluozhai.snowflake.datatable.pojo.Model;
import com.boluozhai.snowflake.rest.element.GitObjectDescriptor;

public class AccountDTM implements Model {

	private String uid;
	private String hashId;
	private String nickname;
	private String description;
	private String email;
	private String location;
	private String language;

	private GitObjectDescriptor avatar;

	public AccountDTM() {
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

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getHashId() {
		return hashId;
	}

	public void setHashId(String hashId) {
		this.hashId = hashId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setAvatar(GitObjectDescriptor avatar) {
		this.avatar = avatar;
	}

	public GitObjectDescriptor getAvatar() {
		return avatar;
	}

}
