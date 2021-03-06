package com.boluozhai.snowflake.xgit.config;

import java.io.IOException;

import com.boluozhai.snowflake.context.MutableProperties;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.XGitComponent;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface Config extends XGitComponent, MutableProperties {

	interface core {

		String filemode = "core.filemode";
		String logallrefupdates = "core.logallrefupdates";
		String repositoryformatversion = "core.repositoryformatversion";
		String symlinks = "core.symlinks";

	}

	interface xgit {

		String enable = "xgit.enable"; // {true|false} ,enable the XGit
										// extensions
		String hashalgorithm = "xgit.hashalgorithm"; // like 'SHA-1'
		String hashpathpattern = "xgit.hashpathpattern"; // like 'xx/xxxx'
		String siterepositorytype = "xgit.siterepositorytype"; // {system|data|user|...}
		String siterepositorydescriptor = "xgit.siterepositorydescriptor"; // obj-id

	}

	interface remote {

		String fetch = "remote..fetch";
		String url = "remote..url";

	}

	interface branch {

		String merge = "branch..merge";
		String remote = "branch..remote";

	}

	void load() throws IOException;

	void save() throws IOException;

	class Factory {

		public static Config getInstance(Repository repo) {
			ComponentContext cc = repo.getComponentContext();
			return (Config) cc.getAttribute(XGitContext.component.config);
		}

	}

}
