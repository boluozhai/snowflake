package com.boluozhai.snowflake.h2o.data.pojo.model;

import java.util.HashSet;
import java.util.Set;

import com.boluozhai.snowflake.h2o.data.pojo.element.AliasItem;

public class Alias {

	private Set<AliasItem> from = new HashSet<AliasItem>();
	private AliasItem to;

	public Set<AliasItem> getFrom() {
		return from;
	}

	public void setFrom(Set<AliasItem> from) {
		this.from = from;
	}

	public AliasItem getTo() {
		return to;
	}

	public void setTo(AliasItem to) {
		this.to = to;
	}

}
