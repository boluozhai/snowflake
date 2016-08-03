package com.boluozhai.snow.webapp.update_system.pojo;

public class MetaFile {

	private PackageInfo remote;
	private PackageInfo cache;
	private PackageInfo installed;

	public PackageInfo getRemote() {
		return remote;
	}

	public void setRemote(PackageInfo remote) {
		this.remote = remote;
	}

	public PackageInfo getCache() {
		return cache;
	}

	public void setCache(PackageInfo cache) {
		this.cache = cache;
	}

	public PackageInfo getInstalled() {
		return installed;
	}

	public void setInstalled(PackageInfo installed) {
		this.installed = installed;
	}

}
