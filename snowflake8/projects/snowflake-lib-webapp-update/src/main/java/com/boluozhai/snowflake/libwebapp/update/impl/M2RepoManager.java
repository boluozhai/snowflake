package com.boluozhai.snowflake.libwebapp.update.impl;

import java.io.File;

import com.boluozhai.snowflake.libwebapp.pojo.WebappPOM;

public class M2RepoManager {

	// private final File _m2;
	private final File _repo_dir;

	public M2RepoManager(File dir) {
		this._repo_dir = new File(dir, "repository");
	}

	public File locateWarFile(WebappPOM pom) {
		// TODO Auto-generated method stub

		File path = this._repo_dir;

		String group = pom.getGroupId();
		String art = pom.getArtifactId();
		String version = pom.getVersion();

		path = this.inner_get_child_path(path, group);
		path = this.inner_get_child_path(path, art);

		final char sep = File.separatorChar;
		final StringBuilder sb = new StringBuilder();
		sb.append(version).append(sep).append(art).append('-').append(version)
				.append(".war");
		path = new File(path, sb.toString());

		if (!path.exists()) {
			throw new RuntimeException("the file not exists: " + path);
		}

		return path;
	}

	private File inner_get_child_path(File base, String offset) {
		File p = base;
		String[] array = offset.split("\\.");
		for (String s : array) {
			s = s.trim();
			if (s.length() > 0) {
				p = new File(p, s);
			}
		}
		return p;
	}
}
