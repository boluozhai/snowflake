package com.boluozhai.snowflake.context.support;

import java.net.URI;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.ContextBuilderFactory;
import com.boluozhai.snowflake.context.MutableContext;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.base.AbstractContext;
import com.boluozhai.snowflake.context.base.AbstractContextBuilder;
import com.boluozhai.snowflake.context.base.SnowContextData;

public class MutableContextBuilderFactory implements ContextBuilderFactory {

	@Override
	public ContextBuilder newBuilder() {
		Builder builder = new Builder(null);
		return builder;
	}

	@Override
	public ContextBuilder newBuilder(SnowflakeContext parent) {
		Builder builder = new Builder(parent);
		return builder;
	}

	private static class Builder extends AbstractContextBuilder {

		protected Builder(SnowflakeContext parent) {
			super(parent);
		}

		@Override
		public SnowflakeContext create() {
			SnowContextData data = this.data();
			return new Context(data);
		}

	}

	private static class Context extends AbstractContext implements
			MutableContext {

		private final SnowContextData _m_data;

		protected Context(SnowContextData data) {
			super(data);
			this._m_data = data;
		}

		@Override
		public void setAttribute(String key, Object value) {
			this._m_data.attr_set.put(key, value);
		}

		@Override
		public void setProperty(String key, String value) {
			this._m_data.prop_set.put(key, value);
		}

		@Override
		public void setParameter(String key, String value) {
			this._m_data.param_set.put(key, value);
		}

		@Override
		public void setEnvironment(String key, String value) {
			this._m_data.env_set.put(key, value);
		}

		@Override
		public void setURI(URI uri) {
			this._m_data.uri = uri;
		}

		@Override
		public void setParent(SnowflakeContext parent) {
			this._m_data.parent = parent;
		}

		@Override
		public void setName(String name) {
			this._m_data.name = name;
		}

		@Override
		public void setDescription(String desc) {
			this._m_data.description = desc;
		}

	}

}
