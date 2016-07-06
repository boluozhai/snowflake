package com.boluozhai.snow.context.support;

import com.boluozhai.snow.context.ContextBuilder;
import com.boluozhai.snow.context.ContextBuilderFactory;
import com.boluozhai.snow.context.SnowContext;
import com.boluozhai.snow.util.SystemAttributes;

public final class AppContextBuilderFactory extends
		AbstractContextBuilderFactory {

	private ContextBuilderFactory getFactoryImpl() {

		try {

			String key = AppContextBuilderFactory.class.getName();
			String cn = SystemAttributes.get(key);

			Class<?> clazz = Class.forName(cn);
			Object factory = clazz.newInstance();
			return (ContextBuilderFactory) factory;

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);

		} catch (InstantiationException e) {
			throw new RuntimeException(e);

		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);

		} finally {
			// NOP
		}

	}

	@Override
	public ContextBuilder newBuilder() {
		return getFactoryImpl().newBuilder();
	}

	@Override
	public ContextBuilder newBuilder(SnowContext parent) {
		return getFactoryImpl().newBuilder(parent);
	}

}
