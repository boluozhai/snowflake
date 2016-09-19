package com.boluozhai.snowflake.access.security.account;

import java.util.Set;

import com.boluozhai.snowflake.access.security.auth.AuthId;

public class Account {

	private AccountId id;
	private String nickname;
	private String email;
	private long registerTime;
	private long birthday;
	private Set<AuthId> authSet;

	public AccountId getId() {
		return id;
	}

	public void setId(AccountId id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(long registerTime) {
		this.registerTime = registerTime;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<AuthId> getAuthSet() {
		return authSet;
	}

	public void setAuthSet(Set<AuthId> authSet) {
		this.authSet = authSet;
	}

}
