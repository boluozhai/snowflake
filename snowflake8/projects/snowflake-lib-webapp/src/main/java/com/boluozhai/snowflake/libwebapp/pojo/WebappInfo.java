package com.boluozhai.snowflake.libwebapp.pojo;

public class WebappInfo {

	private WebappManifest manifest;
	private WebappPOM pom;

	public WebappManifest getManifest() {
		return manifest;
	}

	public void setManifest(WebappManifest manifest) {
		this.manifest = manifest;
	}

	public WebappPOM getPom() {
		return pom;
	}

	public void setPom(WebappPOM pom) {
		this.pom = pom;
	}

}
