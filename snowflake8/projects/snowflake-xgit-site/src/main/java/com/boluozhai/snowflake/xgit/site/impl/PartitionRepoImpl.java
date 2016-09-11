package com.boluozhai.snowflake.xgit.site.impl;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.site.base.AbstractPartitionRepo;
import com.boluozhai.snowflake.xgit.site.wrapper.PartitionRepoWrapper;

public class PartitionRepoImpl extends AbstractPartitionRepo {

	private PartitionRepoImpl(XGitContext context) {
		super(context);
	}

	public static ComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder implements ComponentBuilder {

		@Override
		public void configure(ContextBuilder cb) {
		}

		@Override
		public Component create(ComponentContext cc) {
			XGitContext gc = (XGitContext) cc;
			PartitionRepoImpl inner = new PartitionRepoImpl(gc);
			return new PartitionRepoWrapper(inner);
		}
	}

}
