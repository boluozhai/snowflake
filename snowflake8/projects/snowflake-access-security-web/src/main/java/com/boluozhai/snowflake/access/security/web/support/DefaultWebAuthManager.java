package com.boluozhai.snowflake.access.security.web.support;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.boluozhai.snowflake.access.security.web.auth.WebAuthManager;
import com.boluozhai.snowflake.rest.server.RestController;

public class DefaultWebAuthManager implements WebAuthManager {

	private Map<String, RestController> methods;
	private Map<String, RestController> _safe_methods;

	public DefaultWebAuthManager() {
	}

	public Map<String, RestController> getMethods() {
		return methods;
	}

	public void setMethods(Map<String, RestController> methods) {
		this.methods = methods;
	}

	public Map<String, RestController> get_safe_methods() {
		Map<String, RestController> map = this._safe_methods;
		if (map == null) {
			map = Collections.synchronizedMap(this.methods);
			this._safe_methods = map;
		}
		return map;
	}

	@Override
	public RestController getHandler(String name) {
		Map<String, RestController> map = this.get_safe_methods();
		RestController h = map.get(name);
		if (h == null) {
			throw new RuntimeException("no auth method with name: " + name);
		}
		return h;
	}

	@Override
	public String[] getHandlerNames() {
		Set<String> keys = this.get_safe_methods().keySet();
		return keys.toArray(new String[keys.size()]);
	}

}
