package com.boluozhai.snowflake.access.security.auth;

import com.boluozhai.snowflake.access.security.account.AccountId;

public class AuthInfo {

	private AuthId id; // in usage, like '{type}:{name}'
	private AccountId account;
	private String type; // auth type
	private String name; // user name
	private String privateKey; // password,etc

	public AuthId getId() {
		return id;
	}

	public void setId(AuthId id) {
		this.id = id;
	}

	public AccountId getAccount() {
		return account;
	}

	public void setAccount(AccountId account) {
		this.account = account;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

}
