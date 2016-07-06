package com.boluozhai.snow.context;

import java.net.URI;

public interface MutableContextInfo extends SnowContextInfo {

	public abstract void setURI(URI uri);

	public abstract void setParent(SnowContext parent);

	public abstract void setName(String name);

	public abstract void setDescription(String desc);

}
