package com.boluozhai.snowflake.appserver.pojo;

import com.boluozhai.snowflake.web.pojo.SnowflakeWebappManifest;

public class WebappInfo extends SnowflakeWebappManifest {

	private String warFilePath;

	public String getWarFilePath() {
		return warFilePath;
	}

	public void setWarFilePath(String warFilePath) {
		this.warFilePath = warFilePath;
	}

}
