package com.boluozhai.snowflake.xgit.site.impl;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.site.UserRepository;
import com.boluozhai.snowflake.xgit.site.base.AbstractUserRepo;
import com.boluozhai.snowflake.xgit.site.wrapper.UserRepoWrapper;

final class UserRepoImpl extends AbstractUserRepo {

	private UserRepoImpl(XGitContext context) {
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
			UserRepository inner = new UserRepoImpl(gc);
			return new UserRepoWrapper(inner);
		}
	}

}
