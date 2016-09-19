package com.boluozhai.snowflake.access.security.web.auth.emailpassword;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.api.h2o.AuthModel;
import com.boluozhai.snowflake.rest.element.auth.AuthInfo;
import com.boluozhai.snowflake.rest.server.JsonRestPojoLoader;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.RestServlet.RestInfo;

public class EmailPasswordMethod extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		RestInfo info = this.getRestInfo(request);
		JsonRestPojoLoader ploader = new JsonRestPojoLoader(request);
		AuthModel model = ploader.getPOJO(AuthModel.class);

		model = new AuthModel();
		AuthInfo auth = new AuthInfo();
		model.setResponse(auth);

		auth.setKey("password");
		auth.setMethod("login");
		auth.setName("email@address");
		auth.setStatus("continue");
		auth.setStep("wait_for_password");
		auth.setThread("" + System.currentTimeMillis());

		JsonRestView view = new JsonRestView();
		view.setResponsePOJO(model);
		view.forward(request, response);

	}

}
