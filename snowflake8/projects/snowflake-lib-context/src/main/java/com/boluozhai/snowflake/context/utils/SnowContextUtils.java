package com.boluozhai.snowflake.context.utils;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.ContextBuilderFactory;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.support.DefaultContextBuilderFactory;

public class SnowContextUtils {

	public interface FactoryName {

		String child = "com.boluozhai.snowflake.context.support.ChildContextBuilderFactory";
		String javase = "com.boluozhai.snowflake.spring.support.SpringAppContextBuilderFactory";
		String junit = "com.boluozhai.snowflake.test.support.TestingContextBuilderFactory";
		String webapp = "com.boluozhai.snowflake.web.context.support.SpringWebContextBuilderFactory";

	}

	public interface InitialAttributeName {

		String prefix = InitialAttributeName.class.getName();

		String child = prefix + ".child";

		String junit_target_object = prefix + ".junit.target";

		String javase_app_class = prefix + ".javase.class";
		String javase_app_arg = prefix + ".javase.arg";

		String webapp_servlet_context = prefix + ".webapp.sc";

	}

	public static ContextBuilderFactory getContextBuilderFactory(
			String factoryName) {
		return DefaultContextBuilderFactory.getFactory(factoryName);
	}

	public static ContextBuilder getContextBuilder(String factoryName) {
		return getContextBuilderFactory(factoryName).newBuilder();
	}

	public static ContextBuilder getContextBuilder(SnowContext parent,
			String factoryName) {
		return getContextBuilderFactory(factoryName).newBuilder(parent);
	}

	public static SnowContext getContext(String factoryName) {
		return getContextBuilder(factoryName).create();
	}

	/**********
	 * context for kinds of applications
	 * */

	public static SnowContext getChildContext(SnowContext parent) {
		String fn = FactoryName.child;
		ContextBuilder builder = getContextBuilder(fn);
		builder.setParent(parent);
		return builder.create();
	}

	public static SnowContext getJunitContext(Object junit_target) {
		String fn = FactoryName.junit;
		ContextBuilder builder = getContextBuilder(fn);
		builder.setAttribute(InitialAttributeName.junit_target_object,
				junit_target);
		return builder.create();
	}

	public static SnowContext getAppContext(Class<?> app_class, String[] arg) {
		String fn = FactoryName.javase;
		ContextBuilder builder = getContextBuilder(fn);
		builder.setAttribute(InitialAttributeName.javase_app_class, app_class);
		builder.setAttribute(InitialAttributeName.javase_app_arg, arg);
		return builder.create();
	}

	public static SnowContext getWebApplicationContext(Object servlet_context) {
		String fn = FactoryName.webapp;
		ContextBuilder builder = getContextBuilder(fn);
		builder.setAttribute(InitialAttributeName.webapp_servlet_context,
				servlet_context);
		return builder.create();
	}

}
