package com.boluozhai.snowflake.xgit.site.support;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.site.XGitSite;
import com.boluozhai.snowflake.xgit.site.XGitSiteFactory;
import com.boluozhai.snowflake.xgit.site.impl.XGitSiteImplementation;

public class DefaultXGitSiteFactory implements XGitSiteFactory {

	private XGitSite _site;

	@Override
	public XGitSite getSite(SnowflakeContext context) {

		Class<XGitSiteFactory> type = XGitSiteFactory.class;
		String key = type.getName();
		XGitSiteFactory factory = context.getBean(key, type);
		if (factory instanceof DefaultXGitSiteFactory) {
			DefaultXGitSiteFactory f2 = (DefaultXGitSiteFactory) factory;
			return f2.inner_get_site(context);
		} else {
			return this.inner_get_site(context);
		}

	}

	private XGitSite inner_get_site(SnowflakeContext context) {
		XGitSite site = this._site;
		if (site == null) {
			site = XGitSiteImplementation.createSite(context);
			this._site = site;
		}
		return site;
	}

}
