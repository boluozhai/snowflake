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

public class FileCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		RestRequestInfo rest_info = RestRequestInfo.Factory
				.getInstance(request);
		PathInfo path_info = rest_info.getPathInfo();
		SnowflakeContext context = rest_info.getContext();

		VFS vfs = VFS.Factory.getVFS(context);
		PathPart offset = path_info.getRequiredPart("id");
		VFile base = vfs.newFile(URI.create("file:///"));
		offset = this.normalize(offset);

		FolderView view = new FolderView();
		view.setBaseAtFsRoot(true);
		view.setOffset(offset);
		view.setBase(base.toPath());

		view.handle(request, response);

	}

	private PathPart normalize(PathPart offset) {
		return offset.trim();
	}

}
