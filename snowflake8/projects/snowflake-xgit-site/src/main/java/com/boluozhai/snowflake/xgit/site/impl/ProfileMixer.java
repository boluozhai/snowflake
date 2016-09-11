package com.boluozhai.snowflake.xgit.site.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.support.ContextWrapperFactory;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.support.AbstractRepositoryDriver;
import com.boluozhai.snowflake.xgit.support.RepositoryProfile;

public class ProfileMixer {

	private final RepositoryProfile _buffer;
	private final SnowflakeContext _context;

	public ProfileMixer(SnowflakeContext context) {
		this._context = context;
		this._buffer = new RepositoryProfile();
		this.init();
	}

	private void init() {
		RepositoryProfile pf = this._buffer;
		pf.setAvaliableRepositoryDirectoryNames(this.newStringArray());
		pf.setComponents(this.newHashMap());
		pf.setDefaultProperties(this.newStringMap());
		pf.setFinalProperties(this.newStringMap());
	}

	private Map<String, String> newStringMap() {
		Map<String, String> table = new HashMap<String, String>();
		return Collections.synchronizedMap(table);
	}

	private Map<String, ComponentBuilderFactory> newHashMap() {
		Map<String, ComponentBuilderFactory> table;
		table = new HashMap<String, ComponentBuilderFactory>();
		return Collections.synchronizedMap(table);
	}

	private List<String> newStringArray() {
		List<String> list = new ArrayList<String>();
		return Collections.synchronizedList(list);
	}

	public RepositoryProfile mix() {
		return _buffer;
	}

	public void put(RepositoryProfile profile) {
		this.putDirNames(profile.getAvaliableRepositoryDirectoryNames());
		this.putContextFactory(profile.getComponentContext());
		this.putComponents(profile.getComponents());
		this.putDefaultProp(profile.getDefaultProperties());
		this.putFinalProp(profile.getFinalProperties());
	}

	private void putComponents(Map<String, ComponentBuilderFactory> src) {
		if (src == null) {
			return;
		}
		this._buffer.getComponents().putAll(src);
	}

	private void putDefaultProp(Map<String, String> src) {
		if (src == null) {
			return;
		}
		Map<String, String> dst = this._buffer.getDefaultProperties();
		dst.putAll(src);
	}

	private void putFinalProp(Map<String, String> src) {
		if (src == null) {
			return;
		}
		Map<String, String> dst = this._buffer.getFinalProperties();
		dst.putAll(src);
	}

	private void putContextFactory(ContextWrapperFactory src) {
		if (src == null) {
			return;
		}
		this._buffer.setComponentContext(src);
	}

	private void putDirNames(List<String> src) {
		if (src == null) {
			return;
		}
		List<String> dst = this._buffer.getAvaliableRepositoryDirectoryNames();
		dst.addAll(src);
	}

	public void put(URI uri) {

		SnowflakeContext context = this._context;

		RepositoryManager rm = XGit.getRepositoryManager(context);
		AbstractRepositoryDriver driver = (AbstractRepositoryDriver) rm
				.getDriver(context, uri, null);

		RepositoryProfile profile = driver.getProfile();
		this.put(profile);

	}

}
