package com.boluozhai.snowflake.xgit.utils;

import com.boluozhai.snowflake.context.MutableContext;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface RepositoryAgent {

	Repository getRepository(SnowflakeContext context);

	class Factory {

		public static final String key = RepositoryAgent.class.getName();

		public static void bind(MutableContext context, RepositoryAgent factory) {
			context.setAttribute(key, factory);
		}

		public static RepositoryAgent get(SnowflakeContext context) {
			return context.getBean(key, RepositoryAgent.class);
		}

	}

}
