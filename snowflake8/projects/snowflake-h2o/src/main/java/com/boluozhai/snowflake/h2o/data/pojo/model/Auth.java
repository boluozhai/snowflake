package com.boluozhai.snowflake.h2o.data.pojo.model;

import java.util.HashSet;
import java.util.Set;

import com.boluozhai.snowflake.h2o.data.pojo.element.AuthItem;

public class Auth {

	private Set<AuthItem> items = new HashSet<AuthItem>();

	public Set<AuthItem> getItems() {
		return items;
	}

	public void setItems(Set<AuthItem> items) {
		this.items = items;
	}

}
