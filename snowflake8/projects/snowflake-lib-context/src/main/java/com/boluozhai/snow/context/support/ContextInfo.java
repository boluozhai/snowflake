package com.boluozhai.snow.context.support;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snow.context.SnowContext;

public final class ContextInfo {

	public URI uri;
	public SnowContext parent;
	public String name;
	public String description;

	public Map<String, Object> attr_set;
	public Map<String, String> prop_set;
	public Map<String, String> param_set;

	public ContextInfo() {

		this.attr_set = new HashMap<String, Object>();
		this.prop_set = new HashMap<String, String>();
		this.param_set = new HashMap<String, String>();

	}

	public ContextInfo makeCopy() {

		ContextInfo dst = new ContextInfo();

		dst.uri = this.uri;
		dst.parent = this.parent;
		dst.name = this.name;
		dst.description = this.description;

		this.inner_do_object_copy(this.attr_set, dst.attr_set);
		this.inner_do_string_copy(this.prop_set, dst.prop_set);
		this.inner_do_string_copy(this.param_set, dst.param_set);

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
