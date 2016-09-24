package com.boluozhai.snowflake.h2o.data.pojo.model;

import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.datatable.pojo.Model;
import com.boluozhai.snowflake.h2o.data.pojo.element.AuthItem;

public class Auth implements Model {

	private Map<String, AuthItem> items = new HashMap<String, AuthItem>();

	public Map<String, AuthItem> getItems() {
		return items;
	}

	public void setItems(Map<String, AuthItem> items) {
		this.items = items;
	}

}
