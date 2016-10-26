package com.boluozhai.snowflake.access.security.role;

public interface Role {

	String root = "root";
	String admin = "admin";
	String owner = "owner";
	String friend = "friend";
	String stranger = "stranger";
	String anyone = "anyone";

	String getName();

	String[] listPermissions();

	boolean hasPermission(String pName);

}
