package com.boluozhai.snowflake.context.base;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import com.boluozhai.snowflake.context.SnowContext;

public class SnowContextBase implements SnowContextBaseAPI {

	final static Object no_value_exception = SnowContext.throw_exception_while_nil;

	private final SnowContextData _data;
	private final long _birthday;

	protected SnowContextBase(SnowContextData data) {
		this._birthday = System.currentTimeMillis();
		this._data = data;
	}

	protected SnowContextData data() {
		return _data;
	}

	@Override
	public String getName() {
		return _data.name;
	}

	@Override
	public String getDescription() {
		return _data.description;
	}

	@Override
	public long getBirthday() {
		return this._birthday;
	}

	@Override
	public URI getURI() {
		return this._data.uri;
	}

	@Override
	public SnowContext getParent() {
		return this._data.parent;
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
	public String[] getPropertyNames() {
		Set<String> keys = _data.prop_set.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	@Override
	public String getProperty(String name) {
		return this.getProperty(name, no_value_exception);
	}

	@Override
	public String getProperty(String name, Object defaultValue) {
		Map<String, String> map = _data.prop_set;
		String value = map.get(name);
		if (value == null) {
			if (no_value_exception == defaultValue) {
				String msg = "no property: " + name;
				throw new RuntimeException(msg);
			} else if (defaultValue instanceof String) {
				value = (String) defaultValue;
			}
		}
		return value;
	}

	@Override
	public String[] getAttributeNames() {
		Set<String> keys = _data.attr_set.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	@Override
	public Object getAttribute(String name) {
		return this.getAttribute(name, no_value_exception);
	}

	@Override
	public Object getAttribute(String name, Object defaultValue) {
		Map<String, Object> map = _data.attr_set;
		Object value = map.get(name);
		if (value == null) {
			if (no_value_exception == defaultValue) {
				String msg = "no attribute: " + name;
				throw new RuntimeException(msg);
			} else {
				value = defaultValue;
			}
		}
		return value;
	}

	@Override
	public String[] getParameterNames() {
		Set<String> keys = _data.param_set.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	@Override
	public String getParameter(String name) {
		return this.getParameter(name, no_value_exception);
	}

	@Override
	public String getParameter(String name, Object defaultValue) {
		Map<String, String> map = _data.param_set;
		String value = map.get(name);
		if (value == null) {
			if (no_value_exception == defaultValue) {
				String msg = "no parameter: " + name;
				throw new RuntimeException(msg);
			} else if (defaultValue instanceof String) {
				value = (String) defaultValue;
			}
		}
		return value;
	}

	@Override
	public String[] getEnvironmentNames() {
		Set<String> keys = _data.env_set.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	@Override
	public String getEnvironment(String name) {
		return this.getEnvironment(name, no_value_exception);
	}

	@Override
	public String getEnvironment(String name, Object defaultValue) {
		Map<String, String> map = _data.env_set;
		String value = map.get(name);
		if (value == null) {
			if (no_value_exception == defaultValue) {
				String msg = "no environment: " + name;
				throw new RuntimeException(msg);
			} else if (defaultValue instanceof String) {
				value = (String) defaultValue;
			}
		}
		return value;
	}
}
