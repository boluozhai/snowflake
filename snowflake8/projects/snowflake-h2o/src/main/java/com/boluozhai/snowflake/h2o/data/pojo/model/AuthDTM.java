package com.boluozhai.snowflake.h2o.data.pojo.model;

import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.datatable.pojo.Model;
import com.boluozhai.snowflake.h2o.data.pojo.element.AuthItem;

public class AuthDTM implements Model {

	private Map<String, AuthItem> table = new HashMap<String, AuthItem>();

	public Map<String, AuthItem> getTable() {
		return table;
	}

	public void setTable(Map<String, AuthItem> table) {
		this.table = table;
	}

}
