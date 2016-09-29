package com.boluozhai.snowflake.h2o.data.pojo.model;

import com.boluozhai.snowflake.datatable.pojo.Model;

public class AccountDTM implements Model {

	private String nickname;
	private String email;

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

}
