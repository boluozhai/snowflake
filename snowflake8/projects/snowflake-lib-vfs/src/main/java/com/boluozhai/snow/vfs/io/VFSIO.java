package com.boluozhai.snow.vfs.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.boluozhai.snow.vfs.VFile;
import com.boluozhai.snowflake.context.SnowContext;

public interface VFSIO {

	class Agent {

		public static VFSIO getInstance(SnowContext context) {
			String key = VFSIOFactory.class.getName();
			VFSIOFactory factory = context.getBean(key, VFSIOFactory.class);
			return factory.getVFSIO(context);
		}

	}

	InputStream input(VFile file) throws IOException;

	OutputStream output(VFile file) throws IOException;

}
