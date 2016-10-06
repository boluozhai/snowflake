package com.boluozhai.snowflake.installer.min.pojo;

public class MetaFile extends DocModel {

	private PackageDescriptor remote;
	private PackageDescriptor cache;
	private PackageDescriptor installed;

	public PackageDescriptor getRemote() {
		return remote;
	}

	public void setRemote(PackageDescriptor remote) {
		this.remote = remote;
	}

	public PackageDescriptor getCache() {
		return cache;
	}

	public void setCache(PackageDescriptor cache) {
		this.cache = cache;
	}

	public PackageDescriptor getInstalled() {
		return installed;
	}

	public void setInstalled(PackageDescriptor installed) {
		this.installed = installed;
	}

}
