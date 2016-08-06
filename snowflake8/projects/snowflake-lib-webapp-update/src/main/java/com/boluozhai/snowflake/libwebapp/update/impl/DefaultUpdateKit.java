package com.boluozhai.snowflake.libwebapp.update.impl;

import java.io.File;
import java.net.URI;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.libwebapp.update.UpdateKit;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.vfs.FileRepository;

public class DefaultUpdateKit implements UpdateKit {

	private final SnowContext _context;

	public DefaultUpdateKit(SnowContext context) {
		this._context = context;
	}

	@Override
	public SnowContext getContext() {
		return this._context;
	}

	@Override
	public VFile getWebappsDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileRepository getRepository() {

		// TODO Auto-generated method stub

		AppData ad = this.getAppData();
		File dir = ad.getDataSchemaPath();
		dir = new File(dir, "update-repository.snowflake");

		URI uri = dir.toURI();
		RepositoryManager rm = XGit.getRepositoryManager(_context);
		Repository repo = rm.open(_context, uri, null);
		return (FileRepository) repo;

	}

	@Override
	public URI getRemoteURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppData getAppData() {
		Class<UpdateKit> type = UpdateKit.class;
		SnowContext context = this.getContext();
		AppData ad = AppData.Helper.getInstance(context, type);
		File path = ad.getDataSchemaPath();
		if (!path.exists()) {
			path.mkdirs();
		}
		return ad;
	}

}
