package com.boluozhai.snowflake.xgit.vfs.impl.remotes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.boluozhai.snowflake.context.SnowflakeProperties;
import com.boluozhai.snowflake.vfs.VFSContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.remotes.Remote;
import com.boluozhai.snowflake.xgit.vfs.impl.config.ConfigLoader;
import com.boluozhai.snowflake.xgit.vfs.impl.config.ConfigSaver;

public final class RemoteIO {

	public static Map<String, Remote> load(Map<String, String> src, boolean xgit) {
		Map<String, Remote> dest = new HashMap<String, Remote>();
		Set<String> keys = src.keySet();
		for (String key : keys) {
			String val = src.get(key);
			RemoteProperty rp = RemoteProperty.create(key, val);
			if (rp == null) {
				continue;
			} else {
				rp.appendTo(dest);
			}
		}
		if (xgit) {
			for (Remote it : dest.values()) {
				it.setXgit(xgit);
			}
		}
		return dest;
	}

	public static Map<String, Remote> load(VFile file, boolean xgit)
			throws IOException {

		VFSContext context = file.vfs().context();
		ConfigLoader loader = new ConfigLoader(context);
		Map<String, String> map_ss = loader.load(file);
		return load(map_ss, xgit);
	}

	public static Map<String, Remote> load(Config conf, boolean xgit)
			throws IOException {
		Map<String, String> map_ss = SnowflakeProperties.MapGetter.getMap(conf);
		return load(map_ss, xgit);
	}

	public static Map<String, String> save(Map<String, Remote> src,
			Map<String, String> dest) {
		if (dest == null) {
			dest = new HashMap<String, String>();
		}
		for (String key : src.keySet()) {
			Remote it = src.get(key);
			final String prefix = "remote." + it.getName();
			dest.put((prefix + ".url"), it.getUrl());
			dest.put((prefix + ".fetch"), it.getFetch());
		}
		return dest;
	}

	public static Map<String, String> save(Map<String, Remote> src, VFile file)
			throws IOException {

		Map<String, String> map_ss = null;
		map_ss = save(src, map_ss);

		VFSContext context = file.vfs().context();
		ConfigSaver saver = new ConfigSaver(context);
		saver.save(map_ss, file);

		return map_ss;
	}

	public static Map<String, String> save(Map<String, Remote> src, Config conf)
			throws IOException {

		Map<String, String> map_ss = null;
		map_ss = save(src, map_ss);
		for (String key : map_ss.keySet()) {
			String val = map_ss.get(key);
			conf.setProperty(key, val);
		}
		conf.save();
		return map_ss;
	}

	private static class RemoteProperty {

		private final String remote;
		private final String field;
		private final String value;

		public RemoteProperty(String remote, String field, String value) {
			this.remote = remote;
			this.field = field;
			this.value = value;
		}

		public static RemoteProperty create(String key, String val) {

			if (key == null) {
				return null;
			} else if (key.endsWith(".url")) {
				// ok
			} else if (key.endsWith(".fetch")) {
				// ok
			} else {
				return null;
			}

			if (!key.startsWith("remote.")) {
				return null;
			}

			final String rm_name;
			final String field;
			final int i1 = key.indexOf('.');
			final int i2 = key.lastIndexOf('.');

			if (i1 < i2) {
				rm_name = key.substring(i1 + 1, i2);
				field = key.substring(i2 + 1);
			} else {
				return null;
			}

			return new RemoteProperty(rm_name, field, val);
		}

		public void appendTo(Map<String, Remote> table) {

			Remote item = table.get(this.remote);
			if (item == null) {
				item = new Remote();
				item.setName(this.remote);
				table.put(this.remote, item);
			}

			String f = this.field;
			if (f == null) {
				// NOP

			} else if (f.equals("url")) {
				item.setUrl(this.value);

			} else if (f.equals("fetch")) {
				item.setFetch(this.value);

			} else {
				// NOP
			}

		}
	}

}
