package com.boluozhai.snowflake.libwebapp.update.publisher;

import java.io.File;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.libwebapp.update.impl.DefaultUpdateKit;

public class DefaultPublisherKit extends DefaultUpdateKit implements
		PublisherKit {

	public DefaultPublisherKit(SnowContext context) {
		super(context);
	}

	@Override
	public File getMavenM2dir() {
		AppData ad = this.getAppData();
		String path = ad.getProperty("maven-m2-directory");
		return new File(path);
	}

	@Override
	public File getAppTableJsonFile() {
		AppData ad = this.getAppData();
		String path = ad.getProperty("webapps-table.json");
		return new File(path);
	}

}
