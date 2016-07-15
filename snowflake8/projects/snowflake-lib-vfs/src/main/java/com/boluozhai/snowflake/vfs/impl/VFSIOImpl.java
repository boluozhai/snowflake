package com.boluozhai.snowflake.vfs.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;

public class VFSIOImpl implements VFSIO {

	public VFSIOImpl(SnowContext context) {
	}

	@Override
	public InputStream input(VFile file) throws IOException {
		URI uri = file.toURI();
		File f2 = new File(uri);
		return new FileInputStream(f2);
	}

	@Override
	public OutputStream output(VFile file) throws FileNotFoundException {
		URI uri = file.toURI();
		File f2 = new File(uri);
		return new FileOutputStream(f2);
	}

}
