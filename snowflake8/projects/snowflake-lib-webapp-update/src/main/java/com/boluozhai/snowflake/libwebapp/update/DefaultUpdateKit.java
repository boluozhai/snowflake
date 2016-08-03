package com.boluozhai.snowflake.libwebapp.update;

import java.net.URI;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.vfs.VFile;
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
		return null;
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
		return AppData.Helper.getInstance(context, type);
	}

}
