package com.boluozhai.snowflake.context.support;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.base.AbstractContextBuilder;
import com.boluozhai.snowflake.context.base.AbstractContextBuilderFactory;

public class ChildContextBuilderFactory extends AbstractContextBuilderFactory {

	@Override
	public ContextBuilder newBuilder() {
		return this.get_builder(null);
	}

	@Override
	public ContextBuilder newBuilder(SnowflakeContext parent) {
		return this.get_builder(parent);
	}

	private ContextBuilder get_builder(SnowflakeContext parent) {
		return new Builder(parent);
	}

	private class Builder extends AbstractContextBuilder {

		protected Builder(SnowflakeContext parent) {
			super(parent);
		}
	}

}
