package com.boluozhai.snowflake.context.base;

import java.net.URI;
import java.util.Map;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.SnowflakeAttributes;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.SnowflakeEnvironments;
import com.boluozhai.snowflake.context.SnowflakeParameters;
import com.boluozhai.snowflake.context.SnowflakeProperties;

public class AbstractContextBuilder extends SnowContextBase implements
		ContextBuilder {

	protected AbstractContextBuilder(SnowflakeContext parent) {
		super(make_data(parent));
		this.setParent(parent);
	}

	private static SnowContextData make_data(SnowflakeContext parent) {
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
	public void setParent(SnowflakeContext parent) {

		SnowContextData d = this.data();
		d.parent = parent;

		if (parent != null) {
			d.attr_set.putAll(SnowflakeAttributes.MapGetter.getMap(parent));
			d.prop_set.putAll(SnowflakeProperties.MapGetter.getMap(parent));
			d.param_set.putAll(SnowflakeParameters.MapGetter.getMap(parent));
			d.env_set.putAll(SnowflakeEnvironments.MapGetter.getMap(parent));
		}
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
	public SnowflakeContext create() {
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
