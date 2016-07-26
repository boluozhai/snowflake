package com.boluozhai.snowflake.vfs;

public class VFSTools {

	public static void mkdir4file(VFile file) {
		VFile dir = file.getParentFile();
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	public static void mkdirs4file(VFile file) {
		VFile dir = file.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

}
