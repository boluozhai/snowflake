package com.boluozhai.snowflake.xgit.vfs.impl.objectbank;

import java.io.IOException;
import java.io.OutputStream;

import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;

final class FileObjectBuilderDefault implements GitObjectBuilder {

	private final GitObjectBuilder _target;

	public FileObjectBuilderDefault(FileObjectBankCore core, String type,
			long length) {

		if (length <= FileObjectBankCore.small_object_builder_buffer_size_max) {
			_target = new FileObjectBuilderSmall(core, type, length);
		} else {
			_target = new FileObjectBuilderLarge(core, type, length);
		}

	}

	public String type() {
		return _target.type();
	}

	public long length() {
		return _target.length();
	}

	public void write(byte[] buffer, int offset, int length) throws IOException {
		_target.write(buffer, offset, length);
	}

	public OutputStream getOutputStream() throws IOException {
		return _target.getOutputStream();
	}

	public GitObject create() throws IOException {
		return _target.create();
	}

}
