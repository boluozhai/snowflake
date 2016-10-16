package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.h2o.data.H2oDataTable;
import com.boluozhai.snowflake.h2o.data.dao.AccountDAO;
import com.boluozhai.snowflake.h2o.data.dao.AliasDAO;
import com.boluozhai.snowflake.h2o.data.dao.RepoDAO;
import com.boluozhai.snowflake.h2o.data.pojo.element.RepoItem;
import com.boluozhai.snowflake.h2o.data.pojo.model.AccountDTM;
import com.boluozhai.snowflake.h2o.data.pojo.model.RepoDTM;
import com.boluozhai.snowflake.rest.api.h2o.RepositoryModel;
import com.boluozhai.snowflake.rest.element.account.AccountProfile;
import com.boluozhai.snowflake.rest.element.repository.RepositoryProfile;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.util.IOTools;

public class RepositoryCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		myDoGet handler = new myDoGet();
		handler.load(request);
		RepositoryModel pojo = handler.getResult();

		JsonRestView view = new JsonRestView();
		view.setResponsePOJO(pojo);
		view.handle(request, response);

	}

	private static class myDoGet {

		private RepositoryModel _result;

		public void load(HttpServletRequest request) {

			RestRequestInfo req_info = RestRequestInfo.Factory
					.getInstance(request);
			PathInfo path_info = req_info.getPathInfo();
			String uid = path_info.getPartString("user", true);
			String rpid = path_info.getPartString("id", false);

			if (rpid == null) {
				// NOP
			} else if (rpid.trim().length() == 0) {
				rpid = null;
			} else {
				// NOP
			}

			SnowflakeContext context = req_info.getContext();
			DataClient client = null;

			try {
				client = H2oDataTable.openClient(context);
				AliasDAO alias_dao = new AliasDAO(client);
				RepoDAO repo_dao = new RepoDAO(client);
				AccountDAO account_dao = new AccountDAO(client);

				uid = alias_dao.findUser(uid);
				final AccountDTM account = account_dao.getAccount(uid);

				AccountProfile owner = new AccountProfile();
				owner = this.make_owner_info(account, owner);

				if (rpid == null) {
					// list all
					RepoDTM dm = repo_dao.getRepoModel(uid);
					this.make_result_list(dm, owner);
				} else {
					// detail
					RepoItem dm = repo_dao.findRepo(uid, rpid);
					this.make_result_detail(dm, owner);
				}

			} finally {
				IOTools.close(client);
			}

		}

		private AccountProfile make_owner_info(AccountDTM account,
				AccountProfile owner) {

			if (owner == null) {
				owner = new AccountProfile();
			}

			owner.setUid(account.getUid());
			owner.setNickname(account.getNickname());
			owner.setExists(true);
			owner.setAvatar(account.getAvatar());
			owner.setDescription(null);
			owner.setEmail(null);
			owner.setHashId(account.getHashId());
			owner.setLanguage(account.getLanguage());
			owner.setLocation(account.getLocation());

			return owner;
		}

		private void make_result_list(RepoDTM dm, AccountProfile owner) {

			final List<RepositoryProfile> list;
			final Map<String, RepoItem> table = dm.getTable();
			final String def_repo_name = dm.getDefaultRepository();

			RepositoryProfile def_repo = null;
			List<String> keys = new ArrayList<String>(table.keySet());
			Collections.sort(keys);
			list = new ArrayList<RepositoryProfile>(keys.size());
			for (String key : keys) {
				RepoItem it = table.get(key);
				RepositoryProfile profile = new RepositoryProfile();
				profile = this.convert(it, profile, owner, false);
				if (profile == null) {
					continue;
				}
				if (key.equals(def_repo_name)) {
					def_repo = profile;
					profile.setTheDefault(true);
				}
				list.add(profile);
			}
			RepositoryModel model = new RepositoryModel();
			model.setList(list);
			model.setRepository(def_repo);
			this._result = model;
		}

		private void make_result_detail(RepoItem dm, AccountProfile owner) {
			RepositoryProfile profile = new RepositoryProfile();
			profile = this.convert(dm, profile, owner, true);
			RepositoryModel model = new RepositoryModel();
			model.setRepository(profile);
			this._result = model;
		}

		private RepositoryProfile convert(RepoItem it,
				RepositoryProfile profile, AccountProfile owner, boolean detail) {

			if (profile == null) {
				profile = new RepositoryProfile();
			}

			profile.setName(it.getName());
			profile.setDescription(it.getDescription());
			profile.setExists(true);
			profile.setOwner(owner);
			profile.setUrl(null);
			profile.setIcon(it.getIcon());

			return profile;
		}

		public RepositoryModel getResult() {
			return this._result;
		}

	}

}
