package com.boluozhai.snowflake.h2o.data.pojo;

import java.util.HashSet;
import java.util.Set;

import com.boluozhai.snowflake.datatable.pojo.ForeignKey;

public class Account {

	private String id;
	private String nickname;
	private String email;
	private Set<ForeignKey> authSet;
	private Set<ForeignKey> aliasSet;

	public Account() {
		this.authSet = new HashSet<ForeignKey>();
		this.aliasSet = new HashSet<ForeignKey>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Set<ForeignKey> getAuthSet() {
		return authSet;
	}

	public void setAuthSet(Set<ForeignKey> authSet) {
		this.authSet = authSet;
	}

	public Set<ForeignKey> getAliasSet() {
		return aliasSet;
	}

	public void setAliasSet(Set<ForeignKey> aliasSet) {
		this.aliasSet = aliasSet;
	}

}
