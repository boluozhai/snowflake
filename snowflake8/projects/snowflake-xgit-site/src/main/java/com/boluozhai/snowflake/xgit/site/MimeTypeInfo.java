package com.boluozhai.snowflake.xgit.site;

public class MimeTypeInfo {

	private String mimeType;
	private String suffix;

	// private String majorName;

	public MimeTypeInfo() {
	}

	public MimeTypeInfo(MimeTypeInfo init) {
		if (init != null) {
			this.mimeType = init.mimeType;
			this.suffix = init.suffix;
			// this.majorName = init.majorName;
		}
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
