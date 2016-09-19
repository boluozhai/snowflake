package com.boluozhai.snowflake.access.security.role;

public interface RoleManager {

	Role getRole(String name);

	String[] listRoleNames();

}
