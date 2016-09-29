package com.boluozhai.snowflake.xgit.http.server.controller.service;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.xgit.http.server.GitHttpInfoHolder;
import com.boluozhai.snowflake.xgit.http.server.controller.utils.ServiceHelper;
import com.boluozhai.snowflake.xgit.repository.Repository;

public class XgitUploadObjects extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Repository repo = ServiceHelper.forRepository(request);
		URI location = repo.getComponentContext().getURI();
		response.getOutputStream().println(this + ".with repo::" + location);

	}

	public static class myHolder extends GitHttpInfoHolder {
	}

}
