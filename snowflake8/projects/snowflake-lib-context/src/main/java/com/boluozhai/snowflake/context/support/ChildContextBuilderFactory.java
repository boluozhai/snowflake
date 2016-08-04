package com.boluozhai.snowflake.context.support;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.base.AbstractContextBuilder;
import com.boluozhai.snowflake.context.base.AbstractContextBuilderFactory;

public class ChildContextBuilderFactory extends AbstractContextBuilderFactory {

	@Override
	public ContextBuilder newBuilder() {
		return this.get_builder(null);
	}

	@Override
	public ContextBuilder newBuilder(SnowContext parent) {
		return this.get_builder(parent);
	}

	private ContextBuilder get_builder(SnowContext parent) {
		return new Builder(parent);
	}

	private class Builder extends AbstractContextBuilder {

		protected Builder(SnowContext parent) {
			super(parent);
		}
	}

}
