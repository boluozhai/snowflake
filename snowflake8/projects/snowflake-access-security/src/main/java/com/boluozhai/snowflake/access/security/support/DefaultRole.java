package com.boluozhai.snowflake.access.security.support;

import java.util.Collections;
import java.util.Set;

import com.boluozhai.snowflake.access.security.role.Role;

public class DefaultRole implements Role {

	private String name;
	private Set<String> permissions;
	private final Inner inner;

	public DefaultRole() {
		this.inner = new Inner();
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	private class Inner {

		private Set<String> permissions_safe;

		private synchronized Set<String> load_safe_permissions() {
			Set<String> list = DefaultRole.this.permissions;
			list = Collections.synchronizedSet(list);
			return list;
		}

		private Set<String> get_safe_permissions() {
			Set<String> safe = this.permissions_safe;
			if (safe == null) {
				safe = this.load_safe_permissions();
				this.permissions_safe = safe;
			}
			return safe;
		}
	}

	@Override
	public String[] listPermissions() {
		Set<String> list = this.inner.get_safe_permissions();
		return list.toArray(new String[list.size()]);
	}

	@Override
	public boolean hasPermission(String pName) {
		Set<String> list = this.inner.get_safe_permissions();
		return list.contains(pName);
	}

}
