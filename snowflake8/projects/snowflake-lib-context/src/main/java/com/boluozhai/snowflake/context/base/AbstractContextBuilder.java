package com.boluozhai.snowflake.context.base;

import java.net.URI;
import java.util.Map;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.SnowContext;

public class AbstractContextBuilder extends SnowContextBase implements
		ContextBuilder {

	protected AbstractContextBuilder(SnowContext parent) {
		super(make_data(parent));
	}

	private static SnowContextData make_data(SnowContext parent) {
		final SnowContextData data;
		if (parent == null) {
			data = new SnowContextData();
		} else {
			data = new SnowContextData(parent);
		}
		return data;
	}

	@Override
	public void setURI(URI uri) {
		this.data().uri = uri;
	}

	@Override
	public void setParent(SnowContext parent) {
		this.data().parent = parent;
	}

	@Override
	public void setName(String name) {
		this.data().name = name;
	}

	@Override
	public void setDescription(String desc) {
		this.data().description = desc;
	}

	@Override
	public void setAttribute(String key, Object value) {
		this.data().attr_set.put(key, value);
	}

	@Override
	public void setProperty(String key, String value) {
		this.data().prop_set.put(key, value);
	}

	@Override
	public void setParameter(String key, String value) {
		this.data().param_set.put(key, value);
	}

	@Override
	public void setEnvironment(String key, String value) {
		this.data().env_set.put(key, value);
	}

	@Override
	public SnowContext create() {
		SnowContextData data = this.data();
		return new AbstractContext(data.makeCopy());
	}

	@Override
	public Map<String, String> getParameters() {
		return this.data().param_set;
	}

	@Override
	public Map<String, String> getProperties() {
		return this.data().prop_set;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return this.data().attr_set;
	}

	@Override
	public Map<String, String> getEnvironments() {
		return this.data().env_set;
	}

}
