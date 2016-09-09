package com.boluozhai.snowflake.context.support;

import java.net.URI;

import com.boluozhai.snowflake.context.MutableContext;
import com.boluozhai.snowflake.context.SnowflakeContext;

public class MutableContextWrapper implements MutableContext {

	private final MutableContext _inner;

	public MutableContextWrapper(MutableContext inner) {
		this._inner = inner;
	}

	public String getName() {
		return _inner.getName();
	}

	public String getDescription() {
		return _inner.getDescription();
	}

	public long getBirthday() {
		return _inner.getBirthday();
	}

	public URI getURI() {
		return _inner.getURI();
	}

	public SnowflakeContext getParent() {
		return _inner.getParent();
	}

	public String[] getAttributeNames() {
		return _inner.getAttributeNames();
	}

	public String[] getParameterNames() {
		return _inner.getParameterNames();
	}

	public String[] getPropertyNames() {
		return _inner.getPropertyNames();
	}

	public String[] getEnvironmentNames() {
		return _inner.getEnvironmentNames();
	}

	public Object getBean(String name) {
		return _inner.getBean(name);
	}

	public String getProperty(String name) {
		return _inner.getProperty(name);
	}

	public Object getAttribute(String name) {
		return _inner.getAttribute(name);
	}

	public String getParameter(String name) {
		return _inner.getParameter(name);
	}

	public String getEnvironment(String name) {
		return _inner.getEnvironment(name);
	}

	public <T> T getBean(String name, Class<T> type) {
		return _inner.getBean(name, type);
	}

	public String getProperty(String name, Object defaultValue) {
		return _inner.getProperty(name, defaultValue);
	}

	public Object getAttribute(String name, Object defaultValue) {
		return _inner.getAttribute(name, defaultValue);
	}

	public String getParameter(String name, Object defaultValue) {
		return _inner.getParameter(name, defaultValue);
	}

	public String getEnvironment(String name, Object defaultValue) {
		return _inner.getEnvironment(name, defaultValue);
	}

	public void setAttribute(String key, Object value) {
		_inner.setAttribute(key, value);
	}

	public void setParameter(String key, String value) {
		_inner.setParameter(key, value);
	}

	public void setProperty(String key, String value) {
		_inner.setProperty(key, value);
	}

	public void setEnvironment(String key, String value) {
		_inner.setEnvironment(key, value);
	}

	public void setURI(URI uri) {
		_inner.setURI(uri);
	}

	public void setParent(SnowflakeContext parent) {
		_inner.setParent(parent);
	}

	public void setName(String name) {
		_inner.setName(name);
	}

	public void setDescription(String desc) {
		_inner.setDescription(desc);
	}

}
