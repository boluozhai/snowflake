package com.boluozhai.snowflake.access.security.support;

import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.access.security.role.Role;
import com.boluozhai.snowflake.access.security.role.RoleManager;

public class DefaultRoleManager implements RoleManager {

	private List<Role> roles;
	private final Inner inner;

	public DefaultRoleManager() {
		this.inner = new Inner();
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	private class Inner {

		private Map<String, Role> table;

		private synchronized Map<String, Role> load_table() {
			return null;
		}

		private Map<String, Role> get_table() {
			return null;
		}

		public Role get(String name) {
			// TODO Auto-generated method stub
			return null;
		}

		public String[] listNames() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	@Override
	public Role getRole(String name) {
		return this.inner.get(name);
	}

	@Override
	public String[] listRoleNames() {
		return this.inner.listNames();
	}

}
