package com.boluozhai.snowflake.xgit.site.support;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.xgit.site.impl.XGitSiteImplementation;

public class DataRepoFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return XGitSiteImplementation.newDataRepoBuilder();
	}

}
