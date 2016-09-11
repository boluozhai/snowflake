package com.boluozhai.snowflake.xgit.site.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.site.XGitSite;

final class SiteRepositoryLocatorImpl implements RepositoryLocator {

	@Override
	public URI locate(SnowflakeContext context, URI uri, RepositoryOption option) {

		final String scheme = uri.getScheme();
		RepositoryLocator inner = null;

		if (scheme == null) {
			// NOP
		} else if (scheme.contains("user")) {
			inner = new InnerUserRepoLocator();

		} else if (scheme.contains("system")) {
			inner = new InnerSystemRepoLocator();

		} else if (scheme.contains("data")) {
			inner = new InnerDataRepoLocator();

		} else if (scheme.contains("partition")) {
			inner = new InnerPartitionRepoLocator();

		} else {
			// NOP
		}

		if (inner == null) {
			String msg = "Error: bad site-repo URI: " + uri;
			throw new RuntimeException(msg);
		} else {
			return inner.locate(context, uri, option);
		}

	}

	private class InnerSystemRepoLocator implements RepositoryLocator {

		@Override
		public URI locate(SnowflakeContext context, URI uri,
				RepositoryOption option) {

			Class<?> target = SnowflakeContext.class;
			AppData ad = AppData.Helper.getInstance(context, target);
			String key = "system_repository_uri";
			String value = ad.getProperty(key);
			URI uri2 = URI.create(value);

			if ("file".equals(uri2.getScheme())) {
				return uri2;
			} else {
				String msg = "need a 'file' scheme in URI: " + uri;
				throw new RuntimeException(msg);
			}

		}

	}

	private class InnerDataRepoLocator implements RepositoryLocator {

		@Override
		public URI locate(SnowflakeContext context, URI uri,
				RepositoryOption option) {

			// TODO Auto-generated method stub
			XGitSite site = XGitSite.Agent.getSite(context);

			return null;
		}

	}

	private class InnerPartitionRepoLocator implements RepositoryLocator {

		@Override
		public URI locate(SnowflakeContext context, URI uri,
				RepositoryOption option) {

			// TODO Auto-generated method stub
			XGitSite site = XGitSite.Agent.getSite(context);

			return null;
		}

	}

	private class InnerUserRepoLocator implements RepositoryLocator {

		@Override
		public URI locate(SnowflakeContext context, URI uri,
				RepositoryOption option) {

			// TODO Auto-generated method stub

			XGitSite site = XGitSite.Agent.getSite(context);

			return null;
		}

	}

	private class PathParser {

		private String[] pathElement;

		public void parse(String path) {
			path = path.replace('\\', '/');
			String[] array = path.split("/");
			List<String> list = new ArrayList<String>();
			for (String s : array) {
				if (s == null) {
					continue;
				} else if (s.length() == 0) {
					continue;
				} else {
					list.add(s);
				}
			}
			this.pathElement = list.toArray(new String[list.size()]);
		}

	}

}
