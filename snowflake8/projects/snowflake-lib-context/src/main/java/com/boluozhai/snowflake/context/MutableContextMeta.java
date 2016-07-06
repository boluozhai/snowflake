package com.boluozhai.snowflake.context;

import java.net.URI;

public interface MutableContextMeta extends SnowContextMeta {

	public abstract void setURI(URI uri);

	public abstract void setParent(SnowContext parent);

	public abstract void setName(String name);

	public abstract void setDescription(String desc);

}
