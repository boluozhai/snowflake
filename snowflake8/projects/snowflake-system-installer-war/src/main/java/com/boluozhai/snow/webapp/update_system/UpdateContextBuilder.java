package com.boluozhai.snow.webapp.update_system;

import java.io.File;

import org.springframework.web.context.WebApplicationContext;

import com.boluozhai.snow.webapp.update_system.pojo.BlzSystemUpdateProperties;
import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.libwebapp.server.ApplicationServer;
import com.boluozhai.snowflake.libwebapp.server.ApplicationServerInfo;

public class UpdateContextBuilder {

	// private final WebApplicationContext _app_context;
	private final BlzSystemUpdateProperties _update_properties;

	public UpdateContextBuilder(WebApplicationContext ac) {
		// this._app_context = ac;
		this._update_properties = BlzSystemUpdateProperties.getInstance(ac);
	}

	public UpdateContext create() {

		UpdateContext context = new UpdateContext();

		context.https_only = this._update_properties.isHttpsOnly();
		context.info_url = this._update_properties.getInfoUrl();
		context.webapps_dir = this.inner_get_webapps_dir();
		context.working_dir = this.inner_get_working_dir();

		return context;
	}

	private File inner_get_working_dir() {
		Class<?> schema = this.getClass();
		AppData ad = AppData.Helper.getInstance(schema);
		return ad.getDataSchemaPath();
	}

	private File inner_get_webapps_dir() {
		ApplicationServer as = ApplicationServer.Agent.getServer();
		ApplicationServerInfo as_info = as.getInfo();
		return as_info.getWebappsDir();
	}

}
