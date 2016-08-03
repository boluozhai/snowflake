package com.boluozhai.snowflake.context.support;

import com.boluozhai.snowflake.context.ContextBuilderFactory;
import com.boluozhai.snowflake.context.base.AbstractContextBuilderFactory;

public class ChildContextBuilderFactory extends AbstractContextBuilderFactory {

	public static ContextBuilderFactory getFactory(String factoryName) {
		try {
			Class<?> type = Class.forName(factoryName);
			Object inst = type.newInstance();
			return (ContextBuilderFactory) inst;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			// NOP
		}
	}

}
