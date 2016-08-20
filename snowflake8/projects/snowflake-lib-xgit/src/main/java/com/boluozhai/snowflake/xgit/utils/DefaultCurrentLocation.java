package com.boluozhai.snowflake.xgit.utils;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;

public class DefaultCurrentLocation implements CurrentLocation {

	@Override
	public URI getLocation(SnowflakeContext context) {
		return context.getURI();
	}

}
