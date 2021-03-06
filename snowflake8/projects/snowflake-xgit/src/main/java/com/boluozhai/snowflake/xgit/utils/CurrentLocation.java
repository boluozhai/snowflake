package com.boluozhai.snowflake.xgit.utils;

import java.net.URI;

import com.boluozhai.snowflake.context.MutableAttributes;
import com.boluozhai.snowflake.context.SnowflakeContext;

public interface CurrentLocation {

	URI getLocation(SnowflakeContext context);

	class Factory {

		static final String key = CurrentLocation.class.getName();

		public static CurrentLocation get(SnowflakeContext context) {
			return (CurrentLocation) context.getAttribute(key);
		}

		public static void bind(MutableAttributes context, CurrentLocation inst) {
			context.setAttribute(key, inst);
		}

	}

}
