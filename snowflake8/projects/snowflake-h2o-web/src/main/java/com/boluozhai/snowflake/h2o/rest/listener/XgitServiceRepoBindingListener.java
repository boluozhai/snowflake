package com.boluozhai.snowflake.h2o.rest.listener;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.MutableContext;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.h2o.data.H2oDataTable;
import com.boluozhai.snowflake.h2o.data.dao.AliasDAO;
import com.boluozhai.snowflake.h2o.data.dao.RepoDAO;
import com.boluozhai.snowflake.h2o.data.pojo.element.RepoItem;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.rest.server.support.handler.RestRequestListener;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.utils.RepositoryAgent;

public class XgitServiceRepoBindingListener extends RestRequestListener {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RestRequestInfo req_info = RestRequestInfo.Factory.getInstance(request);
		SnowflakeContext context = req_info.getContext();
		MutableContext mut_context = (MutableContext) context;

		PathInfo path_info = req_info.getPathInfo();
		final String uid = path_info.getPartString("uid");
		final String repoid = path_info.getPartString("repoid");

		DataClient data_client = null;
		try {

			data_client = H2oDataTable.openClient(context);

			AliasDAO alias_dao = new AliasDAO(data_client);
			RepoDAO repo_dao = new RepoDAO(data_client);
			String u2 = alias_dao.findUser(uid);
			RepoItem repo_item = repo_dao.findRepo(u2, repoid);
			String loc = repo_item.getLocation();
			URI uri = URI.create(loc);

			RepositoryAgent agent = new MyRepositoryAgent(uri, uid, u2);
			RepositoryAgent.Factory.bind(mut_context, agent);

		} finally {
			IOTools.close(data_client);
		}

	}

	private static class MyRepositoryAgent implements RepositoryAgent {

		private Repository _repo;
		private final URI _uri;

		public MyRepositoryAgent(URI uri, String uid, String repoid) {
			this._uri = uri;
		}

		@Override
		public Repository getRepository(SnowflakeContext context) {
			Repository repo = this._repo;
			if (repo == null) {
				repo = this.inner_load_repo(context);
				this._repo = repo;
			}
			return repo;
		}

		private Repository inner_load_repo(SnowflakeContext context) {
			RepositoryManager rm = XGit.getRepositoryManager(context);
			return rm.open(context, _uri, null);
		}
	}

}
