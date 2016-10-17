package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.h2o.data.H2oDataTable;
import com.boluozhai.snowflake.h2o.data.dao.AliasDAO;
import com.boluozhai.snowflake.h2o.data.dao.RepoDAO;
import com.boluozhai.snowflake.h2o.data.pojo.element.RepoItem;
import com.boluozhai.snowflake.h2o.rest.view.FolderView;
import com.boluozhai.snowflake.rest.path.PathPart;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.vfs.FileWorkspace;

public class WorkingDirCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Param param = new Param(request);
		VFile base = param.get_base_dir();
		PathPart offset = param.get_offset_part();

		FolderView view = new FolderView();
		view.setBaseAtFsRoot(false);
		view.setOffset(offset.trim());
		view.setBase(base.toPath());

		view.handle(request, response);

	}

	private static class Param {

		public SnowflakeContext context;
		public PathInfo path_info;
		public RestRequestInfo rest_info;
		private HttpServletRequest request;

		public Param(HttpServletRequest request) {

			this.rest_info = RestRequestInfo.Factory.getInstance(request);
			this.path_info = this.rest_info.getPathInfo();
			this.context = this.rest_info.getContext();
			this.request = request;

		}

		public PathPart get_offset_part() throws UnsupportedEncodingException {

			String offset = this.request.getParameter("offset");

			offset = offset.replace('\\', '/');
			String[] array = offset.split("/");
			PathPart pp = new PathPart(array);
			return pp.trim();
		}

		public VFile get_base_dir() {

			DataClient dc = null;

			try {

				final String user_id = this.path_info.getRequiredPart("user")
						.toString();
				final String repo_id = this.path_info.getRequiredPart(
						"repository").toString();

				dc = H2oDataTable.openClient(context);

				AliasDAO alias_dao = new AliasDAO(dc);
				final String user_id_2 = alias_dao.findUser(user_id);
				if (user_id_2 == null) {
					this.exception_not_found(user_id, repo_id);
				}

				RepoDAO repo_dao = new RepoDAO(dc);
				RepoItem item = repo_dao.findRepo(user_id_2, repo_id);
				if (item == null) {
					this.exception_not_found(user_id, repo_id);
				}

				String location = item.getLocation();
				URI uri = URI.create(location);

				RepositoryManager rm = XGit.getRepositoryManager(context);
				Repository repo = rm.open(context, uri, null);
				FileWorkspace working = (FileWorkspace) repo.getWorking();

				return working.getFile();

			} finally {

				IOTools.close(dc);

			}

		}

		private void exception_not_found(String user, String repo) {
			String msg = "the repository named [%s:%s] is not found.";
			msg = String.format(msg, user, repo);
			throw new SnowflakeException(msg);
		}
	}

}
