package com.boluozhai.snowflake.rest.element.viewport;

import com.boluozhai.snowflake.rest.element.account.AccountProfile;
import com.boluozhai.snowflake.rest.element.repository.RepositoryProfile;

public class ViewportProfile {

	private AccountProfile operator;
	private AccountProfile owner;
	private RepositoryProfile repository;
	private String role;

	public AccountProfile getOwner() {
		return owner;
	}

	public void setOwner(AccountProfile owner) {
		this.owner = owner;
	}

	public RepositoryProfile getRepository() {
		return repository;
	}

	public void setRepository(RepositoryProfile repository) {
		this.repository = repository;
	}

	public AccountProfile getOperator() {
		return operator;
	}

	public void setOperator(AccountProfile operator) {
		this.operator = operator;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
