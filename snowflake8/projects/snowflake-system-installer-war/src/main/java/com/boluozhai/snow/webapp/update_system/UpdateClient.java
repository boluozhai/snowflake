package com.boluozhai.snow.webapp.update_system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.boluozhai.snow.webapp.update_system.http.HttpLoader;
import com.boluozhai.snow.webapp.update_system.pojo.MetaFile;
import com.boluozhai.snow.webapp.update_system.pojo.PackageInfo;
import com.boluozhai.snow.webapp.update_system.utils.HashTools;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UpdateClient {

	private final UpdateContext _context;

	public UpdateClient(UpdateContext context) {
		this._context = context;
	}

	public void load_meta() {
		MetaFile meta = null;
		try {
			File path = this._context.getMetaFilePath();
			String txt = TextTools.load(path);
			Gson gs = new Gson();
			meta = gs.fromJson(txt, MetaFile.class);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (meta == null) {
				meta = new MetaFile();
			}
			this._context.meta = meta;
		}
	}

	public void save_meta() {
		try {
			File path = this._context.getMetaFilePath();
			MetaFile meta = this._context.meta;
			GsonBuilder gsb = new GsonBuilder();
			gsb.setPrettyPrinting();
			Gson gs = gsb.create();
			String text = gs.toJson(meta);
			TextTools.save(text, path, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private HttpLoader getHttpLoader() {
		boolean https_only = this._context.https_only;
		return new HttpLoader(https_only);
	}

	public void check() throws IOException {

		HttpLoader htldr = this.getHttpLoader();
		String url = this._context.info_url;
		String str = htldr.loadString(url);

		Properties prop2 = new Properties();
		prop2.load(new StringReader(str));
		Map<String, String> map = new HashMap<String, String>();
		for (Object k : prop2.keySet()) {
			String key = k.toString();
			String value = prop2.getProperty(key);
			map.put(key, value);
		}

		GsonBuilder gsb = new GsonBuilder();
		gsb.setPrettyPrinting();
		Gson gs = gsb.create();

		String txt = gs.toJson(map);
		System.out.println(txt);

		PackageInfo pInfo = gs.fromJson(txt, PackageInfo.class);
		this._context.meta.setRemote(pInfo);

	}

	public void clean() {
	}

	public void download() throws IOException {

		this.check();
		MetaFile meta = this._context.meta;
		PackageInfo remote = meta.getRemote();
		PackageInfo local = meta.getCache();
		local = remote;

		// check local data
		final String hash = local.getSha256sum().toLowerCase();
		final File file = this._context.getWarCachePath(hash);
		if (file.exists()) {
			String h2 = HashTools.sha256(file).toLowerCase();
			if (h2.equalsIgnoreCase(hash)) {
				System.out.println("the cache file exists: " + h2);
				return;
			}
		}

		// download data
		long time = System.currentTimeMillis();
		final File tmp_file = new File(file.getParentFile(), "tmp_" + time
				+ "_" + file.getName());

		String url = remote.getUrl();
		System.out.println("download " + url + " ...");
		HttpLoader loader = this.getHttpLoader();
		byte[] data = loader.loadBytes(url);

		OutputStream out = null;
		try {
			File dir = tmp_file.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			out = new FileOutputStream(tmp_file);
			out.write(data);
		} finally {
			IOTools.close(out);
		}

		if (tmp_file.exists()) {
			String h2 = HashTools.sha256(tmp_file).toLowerCase();
			if (!h2.equalsIgnoreCase(hash)) {
				tmp_file.delete();
				String msg = "error: the hash not match: " + h2 + " | " + hash;
				throw new RuntimeException(msg);
			} else {
				// rename
				tmp_file.renameTo(file);
			}
		}

		// save meta
		meta.setRemote(remote);
		meta.setCache(local);
		this.save_meta();
		System.out.println("download - done: " + file);

	}

	public void install() throws IOException {

		MetaFile meta = this._context.meta;
		PackageInfo cache = meta.getCache();

		String hash = cache.getSha256sum();
		String name = cache.getName() + ".war";

		File src = this._context.getWarCachePath(hash);
		File dst = this._context.getWarDeployPath(name);

		String h2 = HashTools.sha256(src);
		if (h2.equalsIgnoreCase(hash)) {
			// do copy
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new FileInputStream(src);
				out = new FileOutputStream(dst);
				IOTools.pump(in, out);
			} finally {
				IOTools.close(out);
				IOTools.close(in);
			}
		} else {
			String msg = "error: bad hash code: " + name;
			throw new RuntimeException(msg);
		}

		// save meta
		PackageInfo inst = cache;
		meta.setInstalled(inst);
		meta.setCache(cache);
		this.save_meta();
		System.out.println("install war to: " + dst);

	}

	public MetaFile getMeta() {
		return this._context.meta;
	}

}
