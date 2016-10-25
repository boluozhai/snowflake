package com.boluozhai.snowflake.xgit.site.support;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.xgit.site.MimeTypeSet;
import com.boluozhai.snowflake.xgit.site.impl.XGitSiteImplementation;

public class MimeTypeRegistrarFactory implements ComponentBuilderFactory {

	private MimeTypeSet items; // not used

	public MimeTypeSet getItems() {
		return items;
	}

	public void setItems(MimeTypeSet items) {
		this.items = items;
	}

	@Override
	public ComponentBuilder newBuilder() {
		return XGitSiteImplementation.newMimeTypeRegBuilder();
	}

}
