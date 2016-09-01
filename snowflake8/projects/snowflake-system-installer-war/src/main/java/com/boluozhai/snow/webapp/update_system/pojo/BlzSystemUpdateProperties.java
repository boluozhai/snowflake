package com.boluozhai.snow.webapp.update_system.pojo;

import org.springframework.context.ApplicationContext;

public class BlzSystemUpdateProperties {

	private String infoUrl;
	private boolean httpsOnly = true;

	public static BlzSystemUpdateProperties getInstance(ApplicationContext ac) {
		Class<BlzSystemUpdateProperties> clazz = BlzSystemUpdateProperties.class;
		String key = clazz.getName();
		return ac.getBean(key, clazz);
	}

	public String getInfoUrl() {
		return infoUrl;
	}

	public void setInfoUrl(String infoUrl) {
		this.infoUrl = infoUrl;
	}

	public boolean isHttpsOnly() {
		return httpsOnly;
	}

	public void setHttpsOnly(boolean httpsOnly) {
		this.httpsOnly = httpsOnly;
	}

}
