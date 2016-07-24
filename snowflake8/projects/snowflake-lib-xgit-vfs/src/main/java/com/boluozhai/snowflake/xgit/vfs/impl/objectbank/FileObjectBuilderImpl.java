package com.boluozhai.snowflake.xgit.vfs.impl.objectbank;

import java.io.IOException;
import java.io.OutputStream;

import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;

final class FileObjectBuilderImpl implements GitObjectBuilder {

	public FileObjectBuilderImpl(FileObjectBankCore _core) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GitObject create() {
		// TODO Auto-generated method stub
		return null;
	}

}
