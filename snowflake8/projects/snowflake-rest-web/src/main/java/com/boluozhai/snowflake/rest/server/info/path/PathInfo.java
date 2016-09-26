package com.boluozhai.snowflake.rest.server.info.path;

public interface PathInfo {

	PathPart getFullPart();

	PathPart getContextPart();

	PathPart getInAppPart();

	PathPart getPart(String name);

	PathPart getPart(String name, boolean required);

}
