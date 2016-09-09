package com.boluozhai.snowflake.xgit.support;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.support.ContextWrapper;
import com.boluozhai.snowflake.context.support.ContextWrapperFactory;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.Element;
import com.boluozhai.snowflake.xgit.XGitContext;

public class DefaultXGitContextWrapperFactory implements ContextWrapperFactory {

	@Override
	public SnowflakeContext wrap(SnowflakeContext context) {
		return new InnerWrapper(context);
	}

	private static class InnerWrapper extends ContextWrapper implements
			ComponentContext {

		public InnerWrapper(SnowflakeContext inner) {
			super(inner);
		}

		@Override
		public Component getRootComponent() {
			String key = XGitContext.component.repository;
			return this.getBean(key, Component.class);
		}

		@Override
		public Element getElement(String key) {
			return (Element) this.getBean(key);
		}

	}

}
