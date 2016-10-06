package com.boluozhai.snowflake.installer.min.pojo;

import com.boluozhai.snowflake.installer.min.context.ApplicationContext;

public class SpringConfigBean extends DocModel {

	private PackageDescriptor remote;
	private boolean httpsOnly = true;

	public static SpringConfigBean getInstance(ApplicationContext ac) {
		Class<SpringConfigBean> clazz = SpringConfigBean.class;
		String key = clazz.getName();
		return ac.getBean(key, clazz);
	}

	public PackageDescriptor getRemote() {
		return remote;
	}

	public void setRemote(PackageDescriptor remote) {
		this.remote = remote;
	}

	public boolean isHttpsOnly() {
		return httpsOnly;
	}

	public void setHttpsOnly(boolean httpsOnly) {
		this.httpsOnly = httpsOnly;
	}

}
