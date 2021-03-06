package com.boluozhai.snowflake.vfs;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface VFS {

	public static class Factory {

		public static VFS getVFS(SnowflakeContext context) {
			String name = VFSFactory.class.getName();
			VFSFactory factory = context.getBean(name, VFSFactory.class);
			return factory.getVFS(context);
		}

	}

	VFSContext context();

	// factory

	VFile newFile(String path);

	VFile newFile(String dir, String name);

	VFile newFile(VFile dir, String name);

	VFile newFile(URI uri);

	// static

	VFile[] listRoots();

	String separator();

	char separatorChar();

	String pathSeparator();

	char pathSeparatorChar();

}
