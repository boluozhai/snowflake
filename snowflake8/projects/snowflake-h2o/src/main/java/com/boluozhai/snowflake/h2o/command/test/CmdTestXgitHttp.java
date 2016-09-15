package com.boluozhai.snowflake.h2o.command.test;

import java.net.URI;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.rest.REST;
import com.boluozhai.snowflake.rest.client.RESTClient;
import com.boluozhai.snowflake.rest.client.RestAPI;
import com.boluozhai.snowflake.rest.client.RestResource;
import com.boluozhai.snowflake.rest.client.RestService;
import com.boluozhai.snowflake.rest.client.RestType;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.refs.RefManager;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;

public class CmdTestXgitHttp extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		RepositoryManager rm = XGit.getRepositoryManager(context);
		URI uri = this.getTestingURI(context);
		RepositoryOption option = null;
		Repository repo = rm.open(context, uri, option);
		ComponentContext cc = repo.getComponentContext();

		RefManager refs = (RefManager) cc
				.getBean(XGitContext.component.refs);
		ObjectBank bank = (ObjectBank) cc
				.getBean(XGitContext.component.objects);

	}

	private URI getTestingURI(SnowflakeContext context) {

		URI uri = URI.create("http://localhost:18080/h2o/");

		RESTClient client = REST.getClient(context);
		RestService app = client.getService(uri);
		RestAPI api = app.getAPI("xgit");
		RestType type = api.getType("repo");
		RestResource res = type.getResource("test.xgit");

		return res.getURI();
	}
}
