package com.boluozhai.snow.webapp.update_system;

import java.io.File;

import com.boluozhai.snow.webapp.update_system.pojo.MetaFile;

public class UpdateContext {

	public String info_url = null;
	public boolean https_only = true;
	public File working_dir;
	public File webapps_dir;

	public MetaFile meta;

	public File getMetaFilePath() {
		return new File(this.working_dir, "meta.json");
	}

	public File getWarCachePath(String hash) {
		File base = new File(this.working_dir, "cache");
		return new File(base, hash);
	}

	public File getWarDeployPath(String name) {
		return new File(this.webapps_dir, name);
	}

}
