package com.boluozhai.snowflake.h2o.rest.controller.auth;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.cli.CLIUtils;
import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.cli.client.CLIProcess;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.datatable.Transaction;
import com.boluozhai.snowflake.h2o.data.H2oDataTable;
import com.boluozhai.snowflake.h2o.data.dao.AccountDAO;
import com.boluozhai.snowflake.h2o.data.dao.AliasDAO;
import com.boluozhai.snowflake.h2o.data.pojo.element.AliasItem;
import com.boluozhai.snowflake.h2o.data.pojo.element.AuthItem;
import com.boluozhai.snowflake.h2o.data.pojo.element.RepoItem;
import com.boluozhai.snowflake.h2o.data.pojo.model.AccountDTM;
import com.boluozhai.snowflake.h2o.data.pojo.model.AliasDTM;
import com.boluozhai.snowflake.h2o.data.pojo.model.AuthDTM;
import com.boluozhai.snowflake.h2o.data.pojo.model.RepoDTM;
import com.boluozhai.snowflake.h2o.utils.RestCommandAdapter;
import com.boluozhai.snowflake.rest.api.h2o.AuthModel;
import com.boluozhai.snowflake.rest.api.h2o.SessionModel;
import com.boluozhai.snowflake.rest.element.auth.AuthProfile;
import com.boluozhai.snowflake.rest.element.session.SessionProfile;
import com.boluozhai.snowflake.rest.server.JsonRestPojoLoader;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.session.SessionInfo;
import com.boluozhai.snowflake.util.HashTools;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.site.RepositorySpaceAllocator;
import com.boluozhai.snowflake.xgit.site.XGitSite;

public class PasswordAuthMech extends RestController {

	interface DEFINE {

		String mechanism = "password";

	}

	interface METHOD {

		String register = "register";
		String forgetpassword = "forget-password";
		String login = "login";
		String logout = "logout";

	}

	@Override
	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		RestRequestInfo info = this.getRestInfo(request);
		JsonRestPojoLoader ploader = new JsonRestPojoLoader(request);
		AuthModel model = ploader.getPOJO(AuthModel.class);

		MyTaskContext task = new MyTaskContext(info);

		model = task.inner_auth(model, info.getContext());
		model.getRequest().setKey("***");
		model.getRequest().setKey2("***");
		model.getResponse().setKey("***");
		model.getResponse().setKey2("***");
		// String mech = model.getResponse().getMechanism();
		String method = info.getPathInfo().getPartString("id");

		if (method == null) {
			// NOP
		} else if (method.equals(METHOD.logout)) {
			task.inner_clear_session(request, response, model);
		} else if (method.equals(METHOD.login)) {
			boolean ok = model.getResponse().isSuccess();
			boolean done = model.getResponse().isDone();
			if (ok && done) {
				task.inner_make_session(request, response, model);
				task.inner_delay_for_login(300);
			} else {
				task.inner_delay_for_login(2000);
			}
		}

		JsonRestView view = new JsonRestView();
		view.setResponsePOJO(model);
		view.handle(request, response);

	}

	private static class MyTaskContext {

		private final SnowflakeContext _context;

		public MyTaskContext(RestRequestInfo info) {
			this._context = info.getContext();
		}

		public void inner_delay_for_login(int delay) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private void inner_clear_session(HttpServletRequest request,
				HttpServletResponse response, AuthModel auth) {

			RestRequestInfo rest_info = RestRequestInfo.Factory
					.getInstance(request);

			SessionInfo session_info = rest_info.getSessionInfo();
			SessionModel model = session_info.getModel();
			try {
				model.setSession(null);
			} finally {
				session_info.setModel(model);
			}

		}

		private void inner_make_session(HttpServletRequest request,
				HttpServletResponse response, AuthModel auth) {

			RestRequestInfo rest_info = RestRequestInfo.Factory
					.getInstance(request);
			SessionInfo session_info = rest_info.getSessionInfo();

			// data client
			SnowflakeContext context = rest_info.getContext();
			DataClient client = H2oDataTable.openClient(context);
			AliasDAO alias_dao = new AliasDAO(client);
			AccountDAO account_dao = new AccountDAO(client);
			String uid = auth.getResponse().getName();
			uid = alias_dao.findUser(uid);
			AccountDTM account = account_dao.getAccount(uid);

			// model

			SessionModel model = session_info.getModel();
			SessionProfile info = new SessionProfile();
			model.setSession(info);

			info.setAvatar(account.getAvatar());
			info.setDescription(account.getDescription());
			info.setEmail(account.getEmail());
			info.setExists(true);
			info.setHashId(account.getHashId());
			info.setLanguage(account.getLanguage());
			info.setLocation(account.getLocation());
			info.setLogin(true);
			info.setLoginTimestamp(System.currentTimeMillis());
			info.setNickname(account.getNickname());
			info.setUid(uid);

			session_info.setModel(model);

		}

		private AuthModel inner_auth(AuthModel model, SnowflakeContext context) {

			this.inner_hash_psw_again(model);

			final AuthProfile request = model.getRequest();
			final AuthProfile response = new AuthProfile();
			model.setResponse(response);
			final String method = request.getMethod();

			response.setMechanism(request.getMechanism());
			response.setMethod(request.getMethod());
			response.setStatus("undef");

			DataClient dt_client = null;
			Transaction tran = null;
			AuthModel result = null;

			try {

				dt_client = H2oDataTable.openClient(context);
				tran = dt_client.beginTransaction();

				if (method == null) {
					throw new SnowflakeException("bad auth method: " + method);

				} else if (method.equals(METHOD.login)) {
					result = this.inner_login(model, dt_client);

				} else if (method.equals(METHOD.logout)) {
					result = this.inner_logout(model, dt_client);

				} else if (method.equals(METHOD.register)) {
					result = this.inner_register(model, dt_client);

				} else if (method.equals(METHOD.forgetpassword)) {
					result = this.inner_forget_passwd(model, dt_client);

				} else {
					throw new SnowflakeException("bad auth method: " + method);
				}

				tran.commit();

			} catch (Exception e) {

				e.printStackTrace();

				tran.rollback();

				result = model;
				final AuthProfile resp = result.getResponse();
				resp.setDone(true);
				resp.setSuccess(false);
				resp.setCode(e.toString());
				resp.setMessage(e.getMessage());
				resp.setStatus("error");

			} finally {
				IOTools.close(dt_client);
			}

			return result;
		}

		private void inner_hash_psw_again(AuthModel model) {
			AuthProfile req = model.getRequest();
			String key = req.getKey();
			String key2 = HashTools.sha1string(key);
			req.setKey(key2);
		}

		private AuthModel inner_login(AuthModel model, DataClient dc) {

			// parameter
			final AliasDAO alias_dao = new AliasDAO(dc);
			final String passwd = model.getRequest().getKey();
			final String user_name = model.getRequest().getName();
			final String uid = alias_dao.findUser(user_name);

			// data
			AuthDTM auth = dc.get(uid, AuthDTM.class);
			AuthItem auth_item = auth.getTable().get(DEFINE.mechanism);
			final String key = auth_item.getKey();
			final long from = auth_item.getFromTime();
			final long to = auth_item.getToTime();
			final long now = System.currentTimeMillis();

			final AuthProfile res = model.getResponse();
			res.setDone(true);

			if ((now < from) || (to < now)) {
				// out of time
				res.setSuccess(false);
				res.setCode("409");
				res.setMessage("out of time");
				res.setStatus("error");
			} else if (!passwd.equals(key)) {
				// bad passwd
				res.setSuccess(false);
				res.setCode("409");
				res.setMessage("bad username|password");
				res.setStatus("error");
			} else {
				// ok
				res.setSuccess(true);
				res.setCode("200");
				res.setMessage("success");
				res.setStatus("ok");
				res.setName(uid);
			}

			return model;
		}

		private AuthModel inner_register(AuthModel model, DataClient dc)
				throws IOException {

			final String mail = model.getRequest().getName();
			final String uid = mail;
			final String passwd = model.getRequest().getKey();
			final String auth_type = "password";
			final long now = System.currentTimeMillis();
			// about 100 years of age
			final long max_age = (100L * 365 * 24 * 3600 * 1000L);
			final String repo_name = "home";

			final String user_number = this.inner_make_user_number(uid, dc);

			// alias

			final AliasItem mail_alias_item = new AliasItem();
			final AliasItem numb_alias_item = new AliasItem();
			final AliasDTM mail_alias = new AliasDTM(); // the master alias
			final AliasDTM numb_alias = new AliasDTM();

			mail_alias_item.setName(mail);

			numb_alias_item.setName(user_number);

			numb_alias.getFrom();
			numb_alias.setName(user_number);
			numb_alias.setTo(mail_alias_item);

			mail_alias.setTo(null);
			mail_alias.setName(mail);
			mail_alias.getFrom()
					.put(numb_alias_item.getName(), numb_alias_item);
			mail_alias.getFrom()
					.put(mail_alias_item.getName(), mail_alias_item);

			dc.insert(mail_alias.getName(), mail_alias);
			dc.insert(numb_alias.getName(), numb_alias);

			// account
			AccountDTM account = new AccountDTM();
			account.setEmail(mail);
			account.setLanguage("default");
			account.setLocation("undefine");
			account.setNickname(mail);
			account.setUid(user_number);
			account.setAvatar(null);
			account.setDescription("undefine");
			account.setHashId(null);
			account = dc.insert(uid, account);

			// auth
			AuthItem auth_item = new AuthItem();
			auth_item.setType(auth_type);
			auth_item.setName(mail);
			auth_item.setKey(passwd);
			auth_item.setFromTime(now);
			auth_item.setToTime(now + max_age);

			AuthDTM auth = new AuthDTM();
			auth.getTable().put(auth_type, auth_item);
			auth = dc.insert(uid, auth);

			// home repository
			RepoItem repo_item = new RepoItem();
			repo_item.setOwnerUid(mail);
			repo_item.setName(repo_name);
			repo_item.setIcon(null);
			repo_item.setDescription("undefine");
			repo_item.setDescriptor(null);
			repo_item.setLocation(this.inner_git_init_home(repo_item));

			RepoDTM repo = new RepoDTM();
			repo.getTable().put(repo_name, repo_item);
			repo = dc.insert(uid, repo);
			repo.setDefaultRepository(repo_name);

			// set response
			final AuthProfile resp = model.getResponse();
			resp.setDone(true);
			resp.setSuccess(true);
			resp.setCode("ok");
			resp.setMessage("done");
			resp.setStatus("ok");

			return model;
		}

		private String inner_make_user_number(String uid, DataClient dc) {
			final HashTools ht = new HashTools(HashTools.ALGORITHM.sha1);
			final byte[] ba = ht.hash(uid);
			long n = 0;
			for (int i = 8; i > 0; i--) {
				byte b = ba[i];
				n = ((n << 8) | (b & 0xff));
			}
			final String str = Long.toString(n);
			for (int i = 6; i < str.length(); i++) {
				final String id = str.substring(0, i);
				AccountDTM account = dc.get(id, AccountDTM.class);
				if (account == null) {
					return id;
				}
			}
			final String msg = "cannot allocate number for user: " + uid;
			throw new RuntimeException(msg);
		}

		private String inner_git_init_home(RepoItem repo_item)
				throws IOException {

			SnowflakeContext context = _context;

			XGitSite site = XGitSite.Agent.getSite(context);
			RepositorySpaceAllocator allocator = site
					.getRepositorySpaceAllocator();

			String user_name = repo_item.getOwnerUid();
			String repo_name = repo_item.getName();
			String repo_type = RepositorySpaceAllocator.TYPE.user;

			Map<String, String> param = new HashMap<String, String>();
			param.put(RepositorySpaceAllocator.PARAM.repo_type, repo_type);
			param.put(RepositorySpaceAllocator.PARAM.repo_name, repo_name);
			param.put(RepositorySpaceAllocator.PARAM.owner_uid, user_name);

			URI uri = allocator.allocate(param);
			VFS vfs = VFS.Factory.getVFS(context);
			final VFile repo_path = vfs.newFile(uri);
			final VFile cur_path = repo_path.getParentFile();

			// do git init

			RestCommandAdapter cli_adapter = new RestCommandAdapter(context);
			cli_adapter.setPath(cur_path);
			context = cli_adapter.createChildContext();

			String cmd = "git init " + repo_name;
			CLIClient cli = CLIUtils.getClient(context);
			CLIProcess pro = cli.execute(context, cmd);
			pro.hashCode();

			// open new repo
			RepositoryManager xgit = XGit.getRepositoryManager(context);
			Repository repo = xgit.open(context, uri, null);

			return repo.getComponentContext().getURI().toString();
		}

		private AuthModel inner_forget_passwd(AuthModel model, DataClient dc) {
			// TODO Auto-generated method stub
			return model;
		}

		private AuthModel inner_logout(AuthModel model, DataClient dc) {
			// NOP
			return model;
		}

	}

}
