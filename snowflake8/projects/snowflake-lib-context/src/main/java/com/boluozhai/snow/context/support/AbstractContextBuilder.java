package com.boluozhai.snow.context.support;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import com.boluozhai.snow.context.ContextBuilder;
import com.boluozhai.snow.context.SnowContext;

public class AbstractContextBuilder extends ContextBuilder {

	private ContextInfo m_info;

	public AbstractContextBuilder(SnowContext parent) {
		this.m_info = new ContextInfo();
		this.m_info.parent = parent;
	}

	@Override
	public Object getAttribute(String name) {
		return this.m_info.attr_set.get(name);
	}

	@Override
	public String getProperty(String name) {
		return this.m_info.prop_set.get(name);
	}

	@Override
	public String getParameter(String name) {
		return this.m_info.param_set.get(name);
	}

	@Override
	public String[] getAttributeNames() {
		Set<String> keys = this.m_info.attr_set.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	@Override
	public String[] getPropertyNames() {
		Set<String> keys = this.m_info.prop_set.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	@Override
	public String[] getParameterNames() {
		Set<String> keys = this.m_info.param_set.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	@Override
	public SnowContext create() {
		ContextInfo info = this.m_info;
		return new AbstractContext(info);
	}

	@Override
	public SnowContext getParent() {
		return this.m_info.parent;
	}

	@Override
	public Map<String, String> getParameters() {
		return this.m_info.param_set;
	}

	@Override
	public Map<String, String> getProperties() {
		return this.m_info.prop_set;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return this.m_info.attr_set;
	}

	@Override
	public URI getURI() {
		return this.m_info.uri;
	}

	@Override
	public void setURI(URI uri) {
		this.m_info.uri = uri;
	}

	@Override
	public void setParent(SnowContext parent) {
		this.m_info.parent = parent;
	}

	@Override
	public void setAttribute(String key, Object value) {
		this.m_info.attr_set.put(key, value);
	}

	@Override
	public void setParameter(String key, String value) {
		this.m_info.param_set.put(key, value);
	}

	@Override
	public void setProperty(String key, String value) {
		this.m_info.prop_set.put(key, value);
	}

	@Override
	public String getName() {
		return this.m_info.name;
	}

	@Override
	public String getDescription() {
		return this.m_info.description;
	}

	@Override
	public long birthday() {
		return 0;
	}

	@Override
	public Object getBean(String name) {
		return this.m_info.attr_set.get(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String name, Class<T> type) {
		Object obj = this.m_info.attr_set.get(name);
		return (T) obj;
	}

	@Override
	public void setName(String name) {
		this.m_info.name = name;
	}

	@Override
	public void setDescription(String desc) {
		this.m_info.description = desc;
	}

	@Override
	public Object getAttribute(String name, Object defaultValue) {
		Map<String, Object> tab = this.m_info.attr_set;
		Object value = tab.get(name);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	@Override
	public String getProperty(String name, Object defaultValue) {
		Map<String, String> tab = this.m_info.prop_set;
		String value = tab.get(name);
		if (value == null) {
			if (defaultValue != null) {
				value = defaultValue.toString();
			}
		}
		return value;
	}

	@Override
	public String getParameter(String name, Object defaultValue) {
		Map<String, String> tab = this.m_info.param_set;
		String value = tab.get(name);
		if (value == null) {
			if (defaultValue != null) {
				value = defaultValue.toString();
			}
		}
		return value;
	}

}
