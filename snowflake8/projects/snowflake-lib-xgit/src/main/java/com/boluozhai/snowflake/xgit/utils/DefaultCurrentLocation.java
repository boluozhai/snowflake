package com.boluozhai.snowflake.xgit.utils;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowContext;

public class DefaultCurrentLocation implements CurrentLocation {

	@Override
	public URI getLocation(SnowContext context) {
		return context.getURI();
	}

}
