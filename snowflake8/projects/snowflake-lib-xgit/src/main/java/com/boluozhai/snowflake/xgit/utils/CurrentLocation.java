package com.boluozhai.snowflake.xgit.utils;

import java.net.URI;

import com.boluozhai.snowflake.context.MutableContext;
import com.boluozhai.snowflake.context.SnowContext;

public interface CurrentLocation {

	URI getLocation(SnowContext context);

	class Factory {

		static final String key = CurrentLocation.class.getName();

		public static CurrentLocation get(SnowContext context) {
			return (CurrentLocation) context.getAttribute(key);
		}

		public static void bind(MutableContext context, CurrentLocation inst) {
			context.setAttribute(key, inst);
		}

	}

}
