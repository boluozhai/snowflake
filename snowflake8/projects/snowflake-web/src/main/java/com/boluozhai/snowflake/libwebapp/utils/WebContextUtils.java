package com.boluozhai.snowflake.libwebapp.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.MutableContext;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.support.MutableContextBuilderFactory;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;

public class WebContextUtils extends SnowContextUtils {

	// public static SnowflakeContext getWebContext(ServletContext sc) {
	// }

	private static final String key = WebContextUtils.class.getName()
			+ "#snow_context.request-binding";

	public static SnowflakeContext getWebContext(HttpServletRequest request) {

		MutableContext binding = (MutableContext) request.getAttribute(key);

		if (binding == null) {
			binding = BindingBuilder.create(request);
			request.setAttribute(key, binding);
		}

		return binding;

	}

	private static class BindingBuilder {

		public static MutableContext create(HttpServletRequest request) {

			// long time = System.currentTimeMillis();
			// String c_name = BindingBuilder.class.getName();
			// System.out.println(c_name + ".create@time=" + time);

			ServletContext sc = request.getServletContext();
			SnowflakeContext parent = SnowContextUtils
					.getWebApplicationContext(sc);

			MutableContextBuilderFactory factory = new MutableContextBuilderFactory();
			ContextBuilder builder = factory.newBuilder(parent);
			return (MutableContext) builder.create();

		}
	}

}
