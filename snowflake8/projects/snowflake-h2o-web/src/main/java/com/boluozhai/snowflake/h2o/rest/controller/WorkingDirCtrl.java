package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.h2o.rest.view.FolderView;
import com.boluozhai.snowflake.rest.path.PathPart;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;

public class WorkingDirCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Param param = new Param(request);
		VFile base = param.get_base_dir();

		PathPart offset = param.path_info.getRequiredPart("id");

		FolderView view = new FolderView();
		view.setBaseAtFsRoot(false);
		view.setOffset(offset);
		view.setBase(base.toPath());

		view.handle(request, response);

	}

	private static class Param {

		public SnowflakeContext context;
		public PathInfo path_info;
		public RestRequestInfo rest_info;

		public Param(HttpServletRequest request) {

			this.rest_info = RestRequestInfo.Factory.getInstance(request);
			this.path_info = this.rest_info.getPathInfo();
			this.context = this.rest_info.getContext();

		}

		public VFile get_base_dir() {

			String user = this.path_info.getRequiredPart("user").toString();
			String repo = this.path_info.getRequiredPart("repository")
					.toString();

			// TODO Auto-generated method stub

			VFS vfs = VFS.Factory.getVFS(this.context);
			VFile file = vfs.newFile(URI.create("file:///home/"));

			return file.child(user).child(repo);
		}
	}

}
