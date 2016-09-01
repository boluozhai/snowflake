package com.boluozhai.snowflake.libwebapp.support;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.libwebapp.manager.WebAppManager;
import com.boluozhai.snowflake.libwebapp.manager.WebAppManagerFactory;
import com.boluozhai.snowflake.libwebapp.pojo.WebappInfo;
import com.boluozhai.snowflake.libwebapp.server.ApplicationServer;
import com.boluozhai.snowflake.libwebapp.server.ApplicationServerInfo;

public class DefaultWebAppManagerFactory implements WebAppManagerFactory {

	@Override
	public WebAppManager getManager(SnowflakeContext context) {
		return new InnerWebappManager(context);
	}

	private class InnerWebappManager implements WebAppManager {

		private WebappInfo[] _cache;
		private final SnowflakeContext _context;

		public InnerWebappManager(SnowflakeContext context) {
			this._context = context;
		}

		@Override
		public WebappInfo[] loadInstalledWebapps() {
			InnerInfoLoader loader = new InnerInfoLoader(this._context);
			return loader.load();
		}

		@Override
		public WebappInfo[] getInstalledWebapps() {
			WebappInfo[] list = this._cache;
			if (list == null) {
				list = this.loadInstalledWebapps();
				this._cache = list;
			}
			return list;
		}
	}

	private class InnerInfoLoader {

		private final ApplicationServer _app_server;

		public InnerInfoLoader(SnowflakeContext context) {

			this._app_server = ApplicationServer.Agent.getServer();

		}

		public WebappInfo[] load() {

			ApplicationServerInfo as_info = this._app_server.getInfo();
			File webapps = as_info.getWebappsDir();
			if (webapps.exists() && webapps.isDirectory()) {
				// continue;
			} else {
				return new WebappInfo[0];
			}

			String[] names = webapps.list();
			List<WebappInfo> list = new ArrayList<WebappInfo>();

			for (String name : names) {
				if (name.endsWith(".war")) {
					WebappInfo app = new WebappInfo();

					app.setName(name);
					// TODO ...

					list.add(app);
				}
			}

			return list.toArray(new WebappInfo[list.size()]);
		}
	}

}
