package com.boluozhai.snowflake.access.security.permission;

public interface PermissionManager {

	Permission getPermission(String pName);

	String[] listPermissions();

	boolean hasPermission(String pName);

}
