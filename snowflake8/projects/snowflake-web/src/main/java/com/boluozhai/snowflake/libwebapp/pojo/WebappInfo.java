package com.boluozhai.snowflake.libwebapp.pojo;

public class WebappInfo {

	private String name;
	private String warFileURI;
	private String warFileHash;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWarFileURI() {
		return warFileURI;
	}

	public void setWarFileURI(String warFileURI) {
		this.warFileURI = warFileURI;
	}

	public String getWarFileHash() {
		return warFileHash;
	}

	public void setWarFileHash(String warFileHash) {
		this.warFileHash = warFileHash;
	}

}
