package com.boluozhai.snowflake.installer.min;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import com.boluozhai.snowflake.installer.min.context.ApplicationContext;
import com.boluozhai.snowflake.installer.min.http.HttpLoader;
import com.boluozhai.snowflake.installer.min.pojo.MetaFile;
import com.boluozhai.snowflake.installer.min.pojo.PackageDescriptor;
import com.boluozhai.snowflake.installer.min.utils.BinaryTools;
import com.boluozhai.snowflake.installer.min.utils.HashToolsEx;
import com.boluozhai.snowflake.util.HashTools;
import com.boluozhai.snowflake.util.IOTools;
import com.google.gson.Gson;

public class InstallerApp {

	private final InstallerContext context;

	public InstallerApp(InstallerContext context) {
		this.context = context;
	}

	public InstallerApp(ApplicationContext context) {
		this.context = new InstallerContext(context);
	}

	public void install() throws IOException {

		MetaFile meta = this.load();
		Inner in = new Inner();
		in.download_war(meta);
		in.deploy_war(meta);

	}

	public MetaFile load() throws IOException {

		Inner in = new Inner();
		MetaFile meta = new MetaFile();

		meta.setCache(in.load_cached_info());
		meta.setInstalled(in.load_installed_info());
		meta.setRemote(in.load_remote_info());

		return meta;
	}

	private class Inner {

		public PackageDescriptor load_remote_info() throws IOException {

			ApplicationContext ac = InstallerApp.this.context
					.getApplicationContext();
			InstallerConfig conf = InstallerConfig.getInstance(ac);
			PackageDescriptor remote = conf.getRemote();
			String url = remote.getUrl();

			boolean https_only = conf.isHttpsOnly();
			HttpLoader loader = new HttpLoader(https_only);
			String str = loader.loadString(url);

			Gson gs = new Gson();
			PackageDescriptor pd = gs.fromJson(str, PackageDescriptor.class);
			return pd;
		}

		public void deploy_war(MetaFile meta) throws IOException {

			final String hash0 = meta.getRemote().getSha1sum();
			final String hash1 = meta.getCache().getSha1sum();
			final String hash2 = meta.getInstalled().getSha1sum();
			if (hash0.equals(hash1) && hash0.equals(hash2)) {
				System.out.println("nothing updated.");
				return;
			}

			URI src_uri = URI.create(meta.getCache().getUrl());
			URI dst_uri = URI.create(meta.getInstalled().getUrl());

			File src_file = new File(src_uri);
			File dst_file = new File(dst_uri);

			InputStream in = null;
			OutputStream out = null;

			try {
				in = new FileInputStream(src_file);
				out = new FileOutputStream(dst_file);
				IOTools.pump(in, out);
			} finally {
				IOTools.close(out);
				IOTools.close(in);

				System.out.println("deploy " + hash0);
				System.out.println("    from " + src_file);
				System.out.println("      to " + dst_file);

			}

		}

		public void download_war(MetaFile meta) throws IOException {

			ApplicationContext ac = InstallerApp.this.context
					.getApplicationContext();
			InstallerConfig conf = InstallerConfig.getInstance(ac);

			final String url = meta.getRemote().getUrl();
			final String sha1 = meta.getRemote().getSha1sum();
			final int size = meta.getRemote().getSize();

			if (sha1.equals(meta.getCache().getSha1sum())) {
				String msg = "the war file is cached";
				System.out.println(msg);
				return;
			}

			HttpLoader loader = new HttpLoader(conf.isHttpsOnly());
			final byte[] ba = loader.loadBytes(url);
			final String ba_sha1 = HashTools.sha1string(ba);

			if (!sha1.equals(ba_sha1)) {
				String msg = "bad|reg sha1sum: " + ba_sha1 + "|" + sha1;
				throw new RuntimeException(msg);
			}

			if (ba.length != size) {
				throw new RuntimeException("bad size: " + ba.length);
			}

			File file = new File(URI.create(meta.getCache().getUrl()));
			BinaryTools.save(ba, file, true);

			System.out.println("download " + sha1);
			System.out.println("    from " + url);
			System.out.println("    save to " + file);

		}

		public PackageDescriptor load_cached_info() {
			File file = InstallerApp.this.context.getCachedWarFile();
			return this.load_pkg_info_by_file(file);
		}

		public PackageDescriptor load_installed_info() {
			File file = InstallerApp.this.context.getInstalledWarFile();
			return this.load_pkg_info_by_file(file);
		}

		private PackageDescriptor load_pkg_info_by_file(File war_file) {

			PackageDescriptor pd = new PackageDescriptor();

			if (war_file.exists()) {
				pd.setSize((int) war_file.length());
				pd.setSha1sum(HashToolsEx.sha1(war_file));

				// TODO ...

				pd.setName("n_a");
				pd.setDescription("n_a");
				pd.setTimestamp(0);
				pd.setVersion("n");

			}

			pd.setUrl(war_file.toURI().toString());

			return pd;
		}
	}

}
