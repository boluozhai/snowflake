package com.boluozhai.snowflake.libwebapp.update.impl;

import java.io.File;
import java.net.URI;
import java.util.Map;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.libwebapp.pojo.WebappInfo;
import com.boluozhai.snowflake.libwebapp.pojo.WebappPOM;
import com.boluozhai.snowflake.libwebapp.pojo.WebappSet;
import com.boluozhai.snowflake.libwebapp.update.UpdatePublisherKit;

public class DefaultUpdatePublisherKit extends DefaultUpdateKit implements
		UpdatePublisherKit {

	interface PKey {

		String m2dir = "snowflake_update_m2_dir";
		String pom_list = "snowflake_update_pom_list_file";

	}

	public DefaultUpdatePublisherKit(SnowContext context) {
		super(context);
	}

	@Override
	public WebappSet loadWebappPomList() {
		AppData appdata = this.getAppData();
		String pom_list_path = appdata.getProperty(PKey.pom_list);
		File pom_list_file = new File(pom_list_path);
		PomListFileLoader loader = new PomListFileLoader();
		return loader.load(pom_list_file);
	}

	@Override
	public String addWebappsToRepository(WebappSet webapps) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void locateWarFiles(WebappSet webapps) {

		AppData appdata = this.getAppData();
		String m2_path = appdata.getProperty(PKey.m2dir);
		M2RepoManager m2_repo = new M2RepoManager(new File(m2_path));

		Map<String, WebappInfo> table = webapps.getApps();
		for (WebappInfo item : table.values()) {
			WebappPOM pom = item.getPom();
			File war = m2_repo.locateWarFile(pom);
			URI uri = war.toURI();
			item.setWarFileURI(uri.toString());
		}

	}

}
