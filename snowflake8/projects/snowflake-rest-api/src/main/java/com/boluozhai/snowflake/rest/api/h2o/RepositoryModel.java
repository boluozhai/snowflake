package com.boluozhai.snowflake.rest.api.h2o;

import java.util.List;

import com.boluozhai.snowflake.rest.api.RestDoc;
import com.boluozhai.snowflake.rest.element.repository.RepositoryProfile;

public class RepositoryModel extends RestDoc {

	private RepositoryProfile repository;
	private List<RepositoryProfile> list;

	public RepositoryProfile getRepository() {
		return repository;
	}

	public void setRepository(RepositoryProfile repository) {
		this.repository = repository;
	}

	public List<RepositoryProfile> getList() {
		return list;
	}

	public void setList(List<RepositoryProfile> list) {
		this.list = list;
	}

}
