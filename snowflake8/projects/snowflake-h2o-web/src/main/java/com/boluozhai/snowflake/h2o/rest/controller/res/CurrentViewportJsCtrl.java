package com.boluozhai.snowflake.h2o.rest.controller.res;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.h2o.data.H2oDataTable;
import com.boluozhai.snowflake.h2o.data.dao.AliasDAO;
import com.boluozhai.snowflake.h2o.data.pojo.element.RepoItem;
import com.boluozhai.snowflake.h2o.data.pojo.model.AccountDTM;
import com.boluozhai.snowflake.h2o.data.pojo.model.RepoDTM;
import com.boluozhai.snowflake.rest.api.h2o.SessionModel;
import com.boluozhai.snowflake.rest.api.h2o.ViewportModel;
import com.boluozhai.snowflake.rest.element.account.AccountProfile;
import com.boluozhai.snowflake.rest.element.repository.RepositoryProfile;
import com.boluozhai.snowflake.rest.element.session.SessionProfile;
import com.boluozhai.snowflake.rest.element.viewport.ViewportProfile;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.rest.server.info.session.SessionInfo;
import com.boluozhai.snowflake.util.IOTools;
import com.google.gson.GsonBuilder;

public class CurrentViewportJsCtrl extends RestController {

	interface DEFINE {
		String model_name = "com.boluozhai.snowflake.web.Viewport.model";
		String this_class_name = "snowflake.web.WebContextUtils";

	}

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		MyJsBuilder builder = new MyJsBuilder(request);
		builder.load();

		builder.append_this_js();
		builder.append_i18n_js();
		builder.append_viewport_js();

		builder.writeTo(response);

	}

	private static class MyJsBuilder {

		private final HttpServletRequest request;
		private ViewportModel _model;
		private String _this_path;
		private final StringBuilder sb = new StringBuilder();

		public MyJsBuilder(HttpServletRequest request2) {
			this.request = request2;
		}

		public void writeTo(HttpServletResponse response) throws IOException {

			final String str = sb.toString();
			final String enc = "utf-8";
			final byte[] ba = str.getBytes(enc);

			response.setContentType("application/javascript");
			response.setCharacterEncoding(enc);
			response.setContentLength(ba.length);
			response.getOutputStream().write(ba);

		}

		public void append_i18n_js() {
			// TODO Auto-generated method stub

		}

		public void append_viewport_js() {

			final ViewportModel pojo = this._model;

			final String comment = "/* the active viewport info , gen by %s  */\n";
			final GsonBuilder gsb = new GsonBuilder();
			gsb.setPrettyPrinting();

			sb.append(String.format(comment, this.getClass().getName()));
			sb.append(DEFINE.model_name);
			sb.append(" = ");
			sb.append(gsb.create().toJson(pojo));
			sb.append(';');
			sb.append('\n');
			sb.append('\n');

		}

		public void append_this_js() {

			final String comment = "/* the this.js info , gen by %s  */\n";

			sb.append(String.format(comment, this.getClass().getName()));
			sb.append(DEFINE.this_class_name);
			sb.append(".init(function(factory) {");
			sb.append('\n');

			sb.append("\t factory.pathInWebapp('");
			sb.append(this._this_path);
			sb.append("');");
			sb.append('\n');

			sb.append("});");
			sb.append('\n');
			sb.append('\n');

		}

		public void load() {

			final RestRequestInfo rr = RestRequestInfo.Factory
					.getInstance(request);
			ViewportProfile vpt = new ViewportProfile();

			this.load_session_info(rr, vpt);
			this.load_viewport_info(rr, vpt);
			this.load_this_info(rr, vpt);

			ViewportModel model = new ViewportModel();
			model.setViewport(vpt);
			this._model = model;
		}

		private void load_this_info(RestRequestInfo rr, ViewportProfile vpt) {

			PathInfo pi = rr.getPathInfo();
			String[] array = pi.getInAppPart().trimToArray();
			final StringBuilder sb = new StringBuilder();
			final int end = array.length - 1;
			sb.append("~");
			for (int i = 0; i < end; i++) {
				sb.append('/').append('x');
			}
			sb.append("/this.js");

			this._this_path = sb.toString();

		}

		private void load_viewport_info(RestRequestInfo rr, ViewportProfile vpt) {

			AccountProfile owner = null;
			RepositoryProfile repo = null;
			DataClient client = null;

			try {

				// parameter

				final PathInfo pi = rr.getPathInfo();
				String uid = pi.getPartString("user", false);
				String repoid = pi.getPartString("repository", false);

				// dao
				SnowflakeContext context = rr.getContext();
				client = H2oDataTable.openClient(context);
				AliasDAO alias_dao = new AliasDAO(client);
				uid = alias_dao.findUser(uid);
				AccountDTM acc_dtm = client.get(uid, AccountDTM.class);
				RepoDTM repo_dtm = client.get(uid, RepoDTM.class);

				// owner

				if (acc_dtm != null) {
					owner = new AccountProfile();
					final AccountDTM src = acc_dtm;
					owner.setAvatar(src.getAvatar());
					owner.setUid(src.getUid());
					owner.setNickname(src.getNickname());
					owner.setExists(true);
				}
				vpt.setOwner(owner);

				// repo

				if (repo_dtm != null) {
					Map<String, RepoItem> tab = repo_dtm.getTable();
					RepoItem item = null;
					if (tab != null) {
						item = tab.get(repoid);
					}
					if (item != null) {
						repo = new RepositoryProfile();
						repo.setDescription(item.getDescription());
						repo.setIcon(item.getIcon());
						repo.setOwner(owner);
						repo.setExists(true);
						repo.setName(repoid);
						repo.setUrl("todo..." + this);

						// TODO Auto-generated method stub

					}
				}
				vpt.setRepository(repo);

			} finally {
				IOTools.close(client);
			}

		}

		private void load_session_info(RestRequestInfo rr, ViewportProfile vpt) {

			SessionInfo info = null;
			SessionModel model = null;
			SessionProfile profile = null;

			info = rr.getSessionInfo();

			if (info != null) {
				model = info.getModel();
			}

			if (model != null) {
				profile = model.getSession();
			}

			if (profile == null) {
				profile = new SessionProfile();
			}
			profile = this.checkLang(profile);

			vpt.setOperator(profile);

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
					String c = loc.getCountry();
					String l = loc.getLanguage();
					lang = l + '_' + c;
					System.out.println("set default Lang as " + lang);
				}
				// by server (NOP)
				opt.setLanguage(lang);
			}
			return opt;
		}

	}

}
