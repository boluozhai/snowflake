package com.boluozhai.snowflake.h2o.command.sf.repo;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.site.SystemRepository;
import com.boluozhai.snowflake.xgit.site.XGitSite;

public class CmdList extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		XGitSite site = XGitSite.Agent.getSite(context);
		SystemRepository sys_repo = site.getSystemRepository();
		sys_repo.listNames();

	}

}
