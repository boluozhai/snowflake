package com.boluozhai.snowflake.h2o.data.pojo.model;

import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.datatable.pojo.Model;
import com.boluozhai.snowflake.h2o.data.pojo.element.RepoItem;

public class RepoDTM implements Model {

	private Map<String, RepoItem> table = new HashMap<String, RepoItem>();
	private String defaultRepository;

	public String getDefaultRepository() {
		return defaultRepository;
	}

	public void setDefaultRepository(String defaultRepository) {
		this.defaultRepository = defaultRepository;
	}

	public Map<String, RepoItem> getTable() {
		return table;
	}

	public void setTable(Map<String, RepoItem> table) {
		this.table = table;
	}

}
