package com.boluozhai.snowflake.webapp.system.rest.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.libwebapp.manager.WebAppManager;
import com.boluozhai.snowflake.libwebapp.pojo.WebappInfo;
import com.boluozhai.snowflake.libwebapp.utils.WebContextUtils;
import com.boluozhai.snowflake.rest.server.RestController;

public class ModuleCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ServletContext sc = request.getServletContext();
		SnowflakeContext web_con = WebContextUtils.getWebContext(sc);

		WebAppManager webapp_man = WebAppManager.Factory.getManager(web_con);
		WebappInfo[] list = webapp_man.getInstalledWebapps();

		for (WebappInfo info : list) {
			String name = info.getName();
			System.out.println("webapp: " + name);
		}

	}

}
