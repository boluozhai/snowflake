package com.boluozhai.snowflake.h2o.data;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.datatable.DataClientConfiguration;
import com.boluozhai.snowflake.datatable.DataClientFactory;
import com.boluozhai.snowflake.xgit.site.SystemRepository;
import com.boluozhai.snowflake.xgit.site.XGitSite;

public class H2oDataTable {

	public static DataClient openClient(SnowflakeContext context) {
		return getFactory(context).open();
	}

	public static DataClientFactory getFactory(SnowflakeContext context) {
		return holder.get_factory(context);
	}

	private static final FactoryHolder holder;

	static {
		holder = new FactoryHolder();
	}

	private final static class FactoryHolder {

		private DataClientFactory _fast_factory;
		private DataClientFactory _safe_factory;

		public DataClientFactory get_factory(SnowflakeContext context) {
			DataClientFactory factory = this._fast_factory;
			if (factory == null) {
				factory = this.get_safe_factory(context);
				this._fast_factory = factory;
			}
			return factory;
		}

		public synchronized DataClientFactory get_safe_factory(
				SnowflakeContext context) {
			DataClientFactory factory = this._safe_factory;
			if (factory == null) {
				factory = this.load_factory(context);
				this._safe_factory = factory;
			}
			return factory;
		}

		private DataClientFactory load_factory(SnowflakeContext context) {

			XGitSite site = XGitSite.Agent.getSite(context);
			SystemRepository repo = site.getSystemRepository();
			URI uri = repo.getComponentContext().getURI();

			String name = "h2o-data-table";
			DataClientConfiguration conf = (DataClientConfiguration) context
					.getBean(name);
			conf.getDataSource().setLocation(uri.toString());

			System.out.println("open system repo at " + uri);

			return conf.configure(context);
		}

	}

}
