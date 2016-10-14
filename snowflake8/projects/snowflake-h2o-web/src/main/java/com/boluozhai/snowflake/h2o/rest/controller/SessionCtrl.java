package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.datatable.Transaction;
import com.boluozhai.snowflake.h2o.data.H2oDataTable;
import com.boluozhai.snowflake.h2o.data.dao.AccountDAO;
import com.boluozhai.snowflake.h2o.data.pojo.model.AccountDTM;
import com.boluozhai.snowflake.rest.api.h2o.SessionModel;
import com.boluozhai.snowflake.rest.element.account.AccountProfile;
import com.boluozhai.snowflake.rest.element.session.SessionProfile;
import com.boluozhai.snowflake.rest.server.JsonRestPojoLoader;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.session.SessionInfo;
import com.boluozhai.snowflake.util.IOTools;

public class SessionCtrl extends RestController {

	@Override
	protected void rest_put(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		final myDoPut handler = new myDoPut();
		handler.load(request);
		final SessionModel pojo = handler.getResult();
		final JsonRestView view = new JsonRestView();
		view.setResponsePOJO(pojo);
		view.handle(request, response);

	}

	private static class myDoPut {

		private SessionModel _result;

		public SessionModel getResult() {
			return this._result;
		}

		public void load(HttpServletRequest request) throws IOException {

			final JsonRestPojoLoader ploader = new JsonRestPojoLoader(request);

			final SessionModel src = ploader.getPOJO(SessionModel.class);
			final RestRequestInfo req_info = RestRequestInfo.Factory
					.getInstance(request);
			final SessionInfo ses_info = req_info.getSessionInfo();
			final SessionModel ses_model = ses_info.getModel();

			this.update_data_table(src, req_info, ses_info, ses_model);
			this.update_session(src, req_info, ses_info, ses_model);

			this._result = ses_model;

		}

		private void update_session(SessionModel src, RestRequestInfo req_info,
				SessionInfo ses_info, SessionModel dst) {

			if (this.set_lang(src.getSession(), dst.getSession())) {
				ses_info.setModel(dst);
			}

		}

		private void update_data_table(SessionModel src,
				RestRequestInfo req_info, SessionInfo ses_info,
				SessionModel ses_model) {

			final SessionProfile ses_profile = ses_model.getSession();
			if (ses_profile.isExists() && ses_profile.isLogin()) {
				// continue
			} else {
				return;
			}

			DataClient client = null;
			Transaction tx = null;

			try {

				final SnowflakeContext context = req_info.getContext();
				client = H2oDataTable.openClient(context);
				tx = client.beginTransaction();

				final String uid = ses_profile.getUid();
				final AccountDAO acc_dao = new AccountDAO(client);
				final AccountDTM account = acc_dao.getAccount(uid);

				if (this.set_lang(src.getSession(), account)) {
					client.update(account);
					tx.commit();
				}

			} catch (Exception e) {
				tx.rollback();
			} finally {
				IOTools.close(client);
			}

		}

		private boolean set_lang(AccountProfile src, AccountDTM dst) {
			if (src == null || dst == null) {
				return false;
			}
			String lang = src.getLanguage();
			if (lang == null) {
				return false;
			}
			dst.setLanguage(lang);
			return true;
		}

		private boolean set_lang(AccountProfile src, AccountProfile dst) {
			if (src == null || dst == null) {
				return false;
			}
			String lang = src.getLanguage();
			if (lang == null) {
				return false;
			}
			dst.setLanguage(lang);
			return true;
		}

	}

}
