package com.boluozhai.snowflake.libwebapp.update.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.libwebapp.pojo.WebappInfo;
import com.boluozhai.snowflake.libwebapp.pojo.WebappPOM;
import com.boluozhai.snowflake.libwebapp.pojo.WebappSet;
import com.boluozhai.snowflake.util.TextTools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PomListFileLoader {

	public WebappSet load(File file) {

		JsonFileRootPojo root = this.inner_load_pojo(file);
		Map<String, WebappInfo> table = new HashMap<String, WebappInfo>();

		for (String key : root.getSnowflake_webapps_pom_list()) {
			WebappInfo item = this.inner_create_item(key);
			table.put(key, item);
		}

		WebappSet was = new WebappSet();
		was.setApps(table);
		return was;
	}

	private WebappInfo inner_create_item(String key) {

		// TODO Auto-generated method stub

		String[] array = key.split(":");

		String group = array[0];
		String art = array[1];
		String version = array[2];
		String name = array[3];

		WebappPOM pom = new WebappPOM();
		pom.setArtifactId(art);
		pom.setGroupId(group);
		pom.setVersion(version);

		WebappInfo item = new WebappInfo();
		item.setName(name);
		item.setPom(pom);

		return item;

	}

	private JsonFileRootPojo inner_load_pojo(File file) {

		Gson gs = (new GsonBuilder()).create();

		try {
			String str = TextTools.load(file);
			return gs.fromJson(str, JsonFileRootPojo.class);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// NOP
		}

		JsonFileRootPojo root = new JsonFileRootPojo();
		List<String> list = new ArrayList<String>();
		root.setSnowflake_webapps_pom_list(list);

		try {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
				String str = gs.toJson(root);
				TextTools.save(str, file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// NOP
		}

		return root;
	}

	public static class JsonFileRootPojo {

		private List<String> snowflake_webapps_pom_list;

		public List<String> getSnowflake_webapps_pom_list() {
			return snowflake_webapps_pom_list;
		}

		public void setSnowflake_webapps_pom_list(
				List<String> snowflake_webapps_pom_list) {
			this.snowflake_webapps_pom_list = snowflake_webapps_pom_list;
		}

	}

}
