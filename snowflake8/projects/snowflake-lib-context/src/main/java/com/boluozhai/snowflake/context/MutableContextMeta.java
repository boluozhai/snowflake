package com.boluozhai.snowflake.context;

import java.net.URI;

public interface MutableContextMeta extends SnowflakeContextMeta {

	public abstract void setURI(URI uri);

	public abstract void setParent(SnowflakeContext parent);

	public abstract void setName(String name);

	public abstract void setDescription(String desc);

}
