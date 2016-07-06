package com.boluozhai.snowflake.context.base;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.context.SnowAttributes;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.SnowEnvironments;
import com.boluozhai.snowflake.context.SnowParameters;
import com.boluozhai.snowflake.context.SnowProperties;

public final class SnowContextData {

	public URI uri;
	public SnowContext parent;
	public String name;
	public String description;

	public Map<String, Object> attr_set;
	public Map<String, String> prop_set;
	public Map<String, String> param_set;
	public Map<String, String> env_set;

	public SnowContextData() {

		this.attr_set = new HashMap<String, Object>();
		this.prop_set = new HashMap<String, String>();
		this.param_set = new HashMap<String, String>();
		this.env_set = new HashMap<String, String>();

	}

	public SnowContextData(SnowContext parent) {

		this.parent = parent;
		this.uri = parent.getURI();
		this.name = parent.getName();
		this.description = parent.getDescription();

		this.attr_set = SnowAttributes.MapGetter.getMap(parent);
		this.prop_set = SnowProperties.MapGetter.getMap(parent);
		this.param_set = SnowParameters.MapGetter.getMap(parent);
		this.env_set = SnowEnvironments.MapGetter.getMap(parent);

	}

	public SnowContextData makeCopy() {

		SnowContextData dst = new SnowContextData();

		dst.uri = this.uri;
		dst.parent = this.parent;
		dst.name = this.name;
		dst.description = this.description;

		this.inner_do_object_copy(this.attr_set, dst.attr_set);
		this.inner_do_string_copy(this.prop_set, dst.prop_set);
		this.inner_do_string_copy(this.param_set, dst.param_set);
		this.inner_do_string_copy(this.env_set, dst.env_set);

		return dst;
	}

	private void inner_do_string_copy(Map<String, String> src,
			Map<String, String> dst) {

		dst.putAll(src);
	}

	private void inner_do_object_copy(Map<String, Object> src,
			Map<String, Object> dst) {

		dst.putAll(src);
	}

}
