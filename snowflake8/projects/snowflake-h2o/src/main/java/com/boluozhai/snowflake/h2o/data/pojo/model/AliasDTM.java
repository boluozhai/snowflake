package com.boluozhai.snowflake.h2o.data.pojo.model;

import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.datatable.pojo.Model;
import com.boluozhai.snowflake.h2o.data.pojo.element.AliasItem;

public class AliasDTM implements Model {

	private Map<String, AliasItem> from = new HashMap<String, AliasItem>();
	private AliasItem to;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, AliasItem> getFrom() {
		return from;
	}

	public void setFrom(Map<String, AliasItem> from) {
		this.from = from;
	}

	public AliasItem getTo() {
		return to;
	}

	public void setTo(AliasItem to) {
		this.to = to;
	}

}
