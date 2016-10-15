package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;
import java.util.Locale;

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
import com.boluozhai.snowflake.rest.api.h2o.ViewportModel;
import com.boluozhai.snowflake.rest.element.account.AccountProfile;
import com.boluozhai.snowflake.rest.element.repository.RepositoryProfile;
import com.boluozhai.snowflake.rest.element.session.SessionProfile;
import com.boluozhai.snowflake.rest.element.viewport.ViewportProfile;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.rest.server.info.session.SessionInfo;

public class ViewportCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		MyResponseBuilder builder = new MyResponseBuilder(request);
		ViewportModel model = builder.create();

		JsonRestView view = new JsonRestView();
		view.setResponsePOJO(model);
		view.handle(request, response);

	}

	private static class MyResponseBuilder {

		private final HttpServletRequest request;

		public MyResponseBuilder(HttpServletRequest request) {
			this.request = request;
		}

		public ViewportModel create() {
			// TODO Auto-generated method stub

			ViewportProfile viewport = new ViewportProfile();
			ViewportModel model = new ViewportModel();
			model.setViewport(viewport);

			RestRequestInfo req_info = RestRequestInfo.Factory
					.getInstance(request);

			// operator
			SessionInfo session = req_info.getSessionInfo();
			SessionProfile opt = session.getModel().getSession();
			opt = this.checkLang(opt);
			viewport.setOperator(opt);

			// target
			PathInfo path_info = req_info.getPathInfo();
			String user = path_info.getPartString("user", false);
			String repo = path_info.getPartString("repository", false);
			this.fillViewport(viewport, req_info, user, repo);

			return model;
		}

		private SessionProfile checkLang(SessionProfile opt) {
			if (opt == null) {
				opt = new SessionProfile();
			}
			String lang = opt.getLanguage();
			if (lang == null) {
				// by agent
				Locale loc = this.request.getLocale();
				if (loc != null) {
					String c = loc.getISO3Country();
					String l = loc.getISO3Language();
					lang = l + '_' + c;
					System.out.println("set default Lang as " + lang);
				}
				// by server (NOP)
				opt.setLanguage(lang);
			}
			return opt;
		}

		private void fillViewport(ViewportProfile viewport,
				RestRequestInfo req_info, String uid, String rpid) {

			SnowflakeContext context = req_info.getContext();
			DataClient client = H2oDataTable.openClient(context);

			AliasDAO alias_dao = new AliasDAO(client);
			RepoDAO repo_dao = new RepoDAO(client);
			AccountDAO acc_dao = new AccountDAO(client);

			uid = alias_dao.findUser(uid);
			AccountDTM account = acc_dao.getAccount(uid);
			RepoItem repo = repo_dao.findRepo(uid, rpid);

			viewport.setRepository(this.make(account, repo));
			viewport.setOwner(this.make(account));

		}

		private AccountProfile make(AccountDTM account) {

			if (account == null) {
				return null;
			}

			AccountProfile profile = new AccountProfile();

			profile.setAvatar(account.getAvatar());
			profile.setDescription(account.getDescription());
			profile.setEmail(account.getEmail());
			profile.setHashId(account.getHashId());
			profile.setLanguage(account.getLanguage());
			profile.setLocation(account.getLocation());
			profile.setNickname(account.getNickname());
			profile.setUid(account.getUid());
			profile.setExists(true);

			return profile;

		}

		private RepositoryProfile make(AccountDTM owner, RepoItem repo) {

			if ((owner == null) || (repo == null)) {
				return null;
			}

			AccountProfile owner2 = this.make(owner);
			RepositoryProfile profile = new RepositoryProfile();

			profile.setName(repo.getName());
			profile.setIcon(repo.getIcon());
			profile.setDescription(repo.getDescription());
			profile.setOwner(owner2);
			profile.setExists(true);

			return profile;

		}

	}

}
