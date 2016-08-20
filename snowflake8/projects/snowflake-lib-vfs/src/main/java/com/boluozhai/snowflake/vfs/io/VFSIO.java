package com.boluozhai.snowflake.vfs.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.vfs.VFile;

public interface VFSIO {

	class Agent {

		public static VFSIO getInstance(SnowflakeContext context) {
			String key = VFSIOFactory.class.getName();
			VFSIOFactory factory = context.getBean(key, VFSIOFactory.class);
			return factory.getVFSIO(context);
		}

	}

	InputStream input(VFile file) throws IOException;

	OutputStream output(VFile file) throws IOException;

	OutputStream output(VFile file, boolean mkdirs) throws IOException;

}
