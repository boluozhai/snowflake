package com.boluozhai.snow.context.support;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import com.boluozhai.snow.context.SnowContext;

public class AbstractContext implements SnowContext {

	private static final Object def_exception = SnowContext.throw_exception_while_nil;

	private final ContextInfo m_info;
	private final long m_birthday;

	public AbstractContext(ContextInfo info) {
		info = info.makeCopy();
		this.m_info = info;
		this.m_birthday = System.currentTimeMillis();
	}

	@Override
	public Object getAttribute(String name) {
		return this.getAttribute(name, def_exception);
	}

	@Override
	public String getProperty(String name) {
		return this.getProperty(name, def_exception);
	}

	@Override
	public String getParameter(String name) {
		return this.getParameter(name, def_exception);
	}

	@Override
	public String[] getAttributeNames() {
		Set<String> keys = m_info.attr_set.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	@Override
	public String[] getPropertyNames() {
		Set<String> keys = m_info.prop_set.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	@Override
	public String[] getParameterNames() {
		Set<String> keys = m_info.param_set.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	@Override
	public String getName() {
		return m_info.name;
	}

	@Override
	public String getDescription() {
		return m_info.description;
	}

	@Override
	public long birthday() {
		return this.m_birthday;
	}

	@Override
	public URI getURI() {
		return m_info.uri;
	}

	@Override
	public SnowContext getParent() {
		return m_info.parent;
	}

	@Override
	public Object getBean(String name) {
		return this.getAttribute(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String name, Class<T> type) {
		Object attr = this.getAttribute(name);
		return (T) attr;
	}

	@Override
	public String getProperty(String name, Object defaultValue) {
		Map<String, String> map1 = m_info.prop_set;
		String value = map1.get(name);
		if (value == null) {
			if (def_exception == defaultValue) {
				String msg = "no property: " + name;
				throw new RuntimeException(msg);
			} else {
				return defaultValue.toString();
			}
		}
		return value;
	}

	@Override
	public Object getAttribute(String name, Object defaultValue) {
		Map<String, Object> map1 = m_info.attr_set;
		Object value = map1.get(name);
		if (value == null) {
			if (def_exception == defaultValue) {
				String msg = "no attribute: " + name;
				throw new RuntimeException(msg);
			} else {
				return defaultValue;
			}
		}
		return value;
	}

	@Override
	public String getParameter(String name, Object defaultValue) {
		Map<String, String> map1 = m_info.param_set;
		String value = map1.get(name);
		if (value == null) {
			if (def_exception == defaultValue) {
				String msg = "no parameter: " + name;
				throw new RuntimeException(msg);
			} else {
				return defaultValue.toString();
			}
		}
		return value;
	}
}
