package com.boluozhai.snowflake.h2o.data.pojo;

import com.boluozhai.snowflake.datatable.pojo.ForeignKey;

public class AccountAlias {

	private String name;
	private ForeignKey account;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ForeignKey getAccount() {
		return account;
	}

	public void setAccount(ForeignKey account) {
		this.account = account;
	}

}
