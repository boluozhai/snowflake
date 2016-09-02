package com.boluozhai.snowflake.h2o.utils;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;

public class PathElements2VFile {

	private final VFS vfs;

	public PathElements2VFile(SnowflakeContext context) {
		this.vfs = VFS.Factory.getVFS(context);
	}

	public PathElements2VFile(VFS initVFS) {
		this.vfs = initVFS;
	}

	public VFS getVFS() {
		return vfs;
	}

	public VFile getFile(String[] path) {

		final char sep = vfs.separatorChar();
		String[] array = path;
		StringBuilder sb = new StringBuilder();

		if (sep == '/') {
			// unix
			if (array.length > 0) {
				for (String name : array) {
					sb.append(sep);
					sb.append(name);
				}
			} else {
				sb.append(sep);
			}
		} else {
			// windows
			if (array.length > 0) {
				for (String name : array) {
					if (sb.length() > 0) {
						sb.append(sep);
						sb.append(name);
					} else {
						sb.append(name);
						sb.append(sep);
					}
				}
			} else {
				return null;
			}
		}
		return vfs.newFile(sb.toString());
	}

}
