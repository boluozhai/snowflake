package com.boluozhai.snowflake.access.security.web.auth.emailpassword;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.rest.api.h2o.AuthModel;
import com.boluozhai.snowflake.rest.api.h2o.SessionModel;
import com.boluozhai.snowflake.rest.element.auth.AuthInfo;
import com.boluozhai.snowflake.rest.element.session.SessionParam;
import com.boluozhai.snowflake.rest.server.JsonRestPojoLoader;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.util.HashTools;

public class EmailPasswordMethod extends RestController {

	@Override
	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// RestInfo info = this.getRestInfo(request);
		JsonRestPojoLoader ploader = new JsonRestPojoLoader(request);
		AuthModel model = ploader.getPOJO(AuthModel.class);

		model = this.inner_auth(model);
		model.getRequest().setKey("***");
		String method = model.getResponse().getMethod();

		if (method.equals("login")) {
			if (model.getResponse().isSuccess()) {
				this.inner_make_session(request, response, model);
			}
		}

		JsonRestView view = new JsonRestView();
		view.setResponsePOJO(model);
		view.handle(request, response);

	}

	private void inner_make_session(HttpServletRequest request,
			HttpServletResponse response, AuthModel auth) {

		AuthInfo ar = auth.getResponse();
		String name = ar.getName();

		RestRequestInfo rest_info = RestRequestInfo.Factory
				.getInstance(request);
		com.boluozhai.snowflake.rest.server.info.session.SessionInfo session_info = rest_info
				.getSessionInfo();

		SessionModel session = session_info.getModel();
		SessionParam info = new SessionParam();
		session.setSession(info);

		info.setNickname("haha");
		info.setEmail(name);
		info.setLogin(true);
		info.setLoginTimestamp(System.currentTimeMillis());
		info.setHashId("n/a");
		info.setAvatar("n/a");

		session_info.setModel(session);

	}

	private AuthModel inner_auth(AuthModel model) {

		this.inner_hash_psw_again(model);
		final String method = model.getRequest().getMethod();

		AuthInfo response = new AuthInfo();
		model.setResponse(response);
		response.setMethod(method);
		response.setStatus("undef");
		response.setKey("***");

		if (method == null) {
			throw new SnowflakeException("bad auth method: " + method);

		} else if (method.equals("login")) {
			return this.inner_login(model);

		} else if (method.equals("register")) {
			return this.inner_register(model);

		} else if (method.equals("logout")) {
			return this.inner_logout(model);

		} else {
			throw new SnowflakeException("bad auth method: " + method);
		}

	}

	private void inner_hash_psw_again(AuthModel model) {
		AuthInfo req = model.getRequest();
		String key = req.getKey();
		String key2 = HashTools.sha1string(key);
		req.setKey(key2);
	}

	private AuthModel inner_login(AuthModel model) {
		// TODO Auto-generated method stub

		AuthInfo req = model.getRequest();
		AuthInfo res = model.getResponse();

		String pass = req.getKey();
		String user = req.getName();

		String txt = user + ':' + pass;
		String reg = "test@blz:209d5fae8b2ba427d30650dd0250942af944a0c9";
		if (txt.equals(reg)) {
			res.setSuccess(true);
			res.setCode("200");
			res.setMessage("success");
			res.setStatus("ok");
		} else {
			res.setSuccess(false);
			res.setCode("409");
			res.setMessage("bad user|password");
			res.setStatus("error");
		}

		return model;
	}

	private AuthModel inner_register(AuthModel model) {
		// TODO Auto-generated method stub
		return model;
	}

	private AuthModel inner_logout(AuthModel model) {
		// TODO Auto-generated method stub
		return model;
	}

}
