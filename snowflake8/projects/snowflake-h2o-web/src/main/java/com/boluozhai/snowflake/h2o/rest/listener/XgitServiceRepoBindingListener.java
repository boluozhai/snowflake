package com.boluozhai.snowflake.h2o.rest.listener;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.MutableContext;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
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

public class XgitServiceRepoBindingListener implements RestRequestListener {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RestRequestInfo req_info = RestRequestInfo.Factory.getInstance(request);
		SnowflakeContext context = req_info.getContext();
		MutableContext mut_context = (MutableContext) context;

		PathInfo path_info = req_info.getPathInfo();
		final String uid_1 = path_info.getPartString("user", true);
		final String rid_1 = path_info.getPartString("repository", true);

		DataClient data_client = null;
		try {

			data_client = H2oDataTable.openClient(context);

			AliasDAO alias_dao = new AliasDAO(data_client);
			RepoDAO repo_dao = new RepoDAO(data_client);
			final String uid_2 = alias_dao.findUser(uid_1);
			final String rid_2 = this.inner_normalize_repo_id(rid_1);
			RepoItem repo_item = repo_dao.findRepo(uid_2, rid_2);
			if (repo_item == null) {
				String msg = "the repository [%s:%s] is not found.";
				msg = String.format(msg, uid_1, rid_1);
				throw new SnowflakeException(msg);
			}
			String loc = repo_item.getLocation();
			URI uri = URI.create(loc);

			RepositoryAgent agent = new MyRepositoryAgent(uri, uid_2, rid_2);
			RepositoryAgent.Factory.bind(mut_context, agent);

		} finally {
			IOTools.close(data_client);
		}

	}

	private String inner_normalize_repo_id(final String repo_id) {

		final int index = repo_id.lastIndexOf('.');
		if (index < 0) {
			return repo_id;
		}

		String p1 = repo_id.substring(0, index);
		String p2 = repo_id.substring(index).toLowerCase();

		String[] array = { ".sf", ".git", ".xgit", ".snow", ".snowflake" };
		for (String s : array) {
			if (p2.equals(s)) {
				return p1;
			}
		}

		return repo_id;
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
