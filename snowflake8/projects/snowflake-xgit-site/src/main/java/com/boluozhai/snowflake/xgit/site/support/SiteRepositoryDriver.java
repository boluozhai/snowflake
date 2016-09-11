package com.boluozhai.snowflake.xgit.site.support;

import java.net.URI;
import java.util.LinkedList;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.site.impl.ProfileMixer;
import com.boluozhai.snowflake.xgit.site.impl.XGitSiteImplementation;
import com.boluozhai.snowflake.xgit.support.AbstractRepositoryDriver;
import com.boluozhai.snowflake.xgit.support.OpenRepositoryParam;
import com.boluozhai.snowflake.xgit.support.RepositoryLoader;
import com.boluozhai.snowflake.xgit.support.RepositoryProfile;

public class SiteRepositoryDriver extends AbstractRepositoryDriver {

	private ProfileHolder profile_holder;

	@Override
	public Repository open(SnowflakeContext context, URI uri,
			RepositoryOption option) {

		ProfileHolder profile_holder = this.get_profile_holder();

		OpenRepositoryParam param = new OpenRepositoryParam();
		param.context = context;
		param.uri = uri;
		param.option = option;
		param.profile = profile_holder.getProfile(context);

		RepositoryLoader loader = XGitSiteImplementation.createRepoLoader();
		return loader.load(param);
	}

	private ProfileHolder get_profile_holder() {
		ProfileHolder ph = this.profile_holder;
		if (ph == null) {
			ph = new ProfileHolder();
			this.profile_holder = ph;
		}
		return ph;
	}

	@Override
	public RepositoryLocator getLocator() {
		return XGitSiteImplementation.createRepoLocator();
	}

	private class ProfileHolder {

		private RepositoryProfile _profile;

		public RepositoryProfile load_prefile(SnowflakeContext context) {

			LinkedList<RepositoryProfile> list = new LinkedList<RepositoryProfile>();
			RepositoryProfile pf = SiteRepositoryDriver.this.getProfile();

			for (int tout = 30; pf != null; tout--) {
				list.push(pf);
				if (tout < 0) {
					throw new RuntimeException("the profile chain too long.");
				} else {
					pf = pf.getParent();
				}
			}

			ProfileMixer mixer = new ProfileMixer(context);

			for (; list.size() > 0;) {
				pf = list.pop();
				mixer.put(pf);
			}

			return mixer.mix();
		}

		public RepositoryProfile getProfile(SnowflakeContext context) {
			RepositoryProfile pf = this._profile;
			if (pf == null) {
				pf = this.load_prefile(context);
				this._profile = pf;
			}
			return pf;
		}

	}

}
