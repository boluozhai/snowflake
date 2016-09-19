package com.boluozhai.snowflake.access.security.role;

public interface Role {

	String getName();

	String[] listPermissions();

	boolean hasPermission(String pName);

}
