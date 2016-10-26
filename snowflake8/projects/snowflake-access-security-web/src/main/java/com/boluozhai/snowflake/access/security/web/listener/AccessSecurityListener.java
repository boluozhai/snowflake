package com.boluozhai.snowflake.access.security.web.listener;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.access.security.role.Role;
import com.boluozhai.snowflake.access.security.role.RoleManager;
import com.boluozhai.snowflake.access.security.web.PermissionComputer;
import com.boluozhai.snowflake.access.security.web.RoleComputer;
import com.boluozhai.snowflake.rest.server.support.handler.RestRequestListener;

public class AccessSecurityListener implements RestRequestListener {

	private RoleManager roleManager;
	private RoleComputer roleComputer;
	private PermissionComputer permissionComputer;

	public AccessSecurityListener() {
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final String[] roleNames = this.roleComputer.compute(request);
		final String perm = this.permissionComputer.compute(request);
		final RoleManager rm = this.roleManager;
		for (String roleName : roleNames) {
			final Role role = rm.getRole(roleName);
			if (role == null) {
				String msg = "no role for name:" + roleName;
				throw new RuntimeException(msg);
			} else if (role.hasPermission(perm)) {
				return;
			}
		}
		String msg = "no permission:" + perm;
		throw new RuntimeException(msg);
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public RoleComputer getRoleComputer() {
		return roleComputer;
	}

	public void setRoleComputer(RoleComputer roleComputer) {
		this.roleComputer = roleComputer;
	}

	public PermissionComputer getPermissionComputer() {
		return permissionComputer;
	}

	public void setPermissionComputer(PermissionComputer permissionComputer) {
		this.permissionComputer = permissionComputer;
	}

}
