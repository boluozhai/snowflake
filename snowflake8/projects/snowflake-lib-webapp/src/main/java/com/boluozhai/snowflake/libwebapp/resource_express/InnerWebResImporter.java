package com.boluozhai.snowflake.libwebapp.resource_express;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;
import com.boluozhai.snowflake.util.DirTools;
import com.boluozhai.snowflake.util.IOTools;

final class InnerWebResImporter extends WebResImporter {

	@Override
	public void play(WebResDescriptor descriptor) {

		try {

			Task task = new Task(descriptor);
			task.getContext();

			Dest dest = new Dest();
			dest.load(descriptor);
			dest.clean();

			List<WebResItem> items = descriptor.getItems();
			for (WebResItem it : items) {
				Src src = new Src(task);
				src.load(it);
				src.writeTo(dest);
			}

		} catch (ZipException e) {

			throw new RuntimeException(e);

		} catch (IOException e) {

			throw new RuntimeException(e);

		} finally {
		}

	}

	private class Task {

		private File _m2_dir;
		private SnowflakeContext _context;
		private final WebResDescriptor _descriptor;

		public Task(WebResDescriptor desc) {
			this._descriptor = desc;
		}

		public File getWar(WebResItem item) {

			File m2 = this.getM2Dir();

			final char sep = File.separatorChar;
			String gid = item.getGroupId();
			String aid = item.getArtifactId();
			String ver = item.getVersion();

			gid = gid.replace('.', sep);
			aid = aid.replace('.', sep);

			StringBuilder path = new StringBuilder();
			path.append("repository").append(sep);
			path.append(gid).append(sep);
			path.append(aid).append(sep);
			path.append(ver).append(sep);
			path.append(item.getArtifactId()).append('-').append(ver)
					.append(".war");

			return new File(m2, path.toString());

		}

		private File getM2Dir() {
			File m2 = this._m2_dir;
			if (m2 == null) {
				String path = this.getM2DirPath();
				m2 = new File(path);
				this._m2_dir = m2;
			}
			return m2;
		}

		private String getM2DirPath() {
			SnowflakeContext context = this.getContext();
			Class<? extends Task> tar = this.getClass();
			AppData ad = AppData.Helper.getInstance(context, tar);
			return ad.getProperty("maven_m2_dir");
		}

		private SnowflakeContext getContext() {
			SnowflakeContext context = this._context;
			if (context == null) {
				Class<?> app_class = WebResImporter.class;
				String[] arg = {};
				context = SnowContextUtils.getAppContext(app_class, arg);
				this._context = context;
			}
			return context;
		}
	}

	private class Dest {

		private File _pom_xml;
		private File _target_dir;

		public void load(WebResDescriptor descriptor) {

			try {
				final URL loc = descriptor.getClass().getProtectionDomain()
						.getCodeSource().getLocation();
				final String find4 = "pom.xml";
				final File code = new File(loc.toURI());

				// System.out.println("dest: " + loc);

				File p = code;
				for (int tout = 100; p != null; p = p.getParentFile(), tout--) {
					if (tout < 0) {
						throw new RuntimeException("timeout");
					}
					File file = new File(p, find4);
					if (file.exists() && file.isFile()) {
						p = file;
						break;
					}
				}

				if (p == null) {
					String msg = "cannot find [%s] in path of [%s]";
					msg = String.format(msg, find4, code);
					throw new RuntimeException(msg);
				}

				String path = descriptor.getPathInProject();

				this._pom_xml = p;
				this._target_dir = new File(p.getParentFile(), path);

			} catch (URISyntaxException e) {

				throw new RuntimeException(e);

			} finally {

				System.out.println("for " + this._pom_xml);

			}
		}

		public void clean() {
			File dir = this._target_dir;
			System.out.println("clean " + dir);
			DirTools.clean(dir);
		}

		public File getTargetFile(String name) {
			File base = this._target_dir;
			return new File(base, name);
		}
	}

	private class Src {

		private final Task _task;
		private File _war;
		private String _path_in_war;

		public Src(Task task) {
			this._task = task;
		}

		public void load(WebResItem item) {

			// location war
			File war = _task.getWar(item);
			String prefix = item.getPathInWar();

			if (!prefix.endsWith("/")) {
				prefix = prefix + '/';
			}

			System.out.format("load %s@%s\n", war, prefix);

			this._war = war;
			this._path_in_war = prefix;

		}

		public void writeTo(Dest dest) throws ZipException, IOException {
			ZipFile zip = null;
			try {
				zip = new ZipFile(_war);
				Enumeration<? extends ZipEntry> ents = zip.entries();
				for (; ents.hasMoreElements();) {
					ZipEntry ent = ents.nextElement();
					if (!ent.isDirectory()) {
						this.writeTo(dest, zip, ent);
					}
				}
			} finally {
				IOTools.close(zip);
			}
		}

		private void writeTo(Dest dest, ZipFile zip, ZipEntry ent)
				throws IOException {

			final String prefix = this._path_in_war;
			String name = ent.getName();
			if (name.startsWith(prefix)) {
				name = name.substring(prefix.length());
			} else {
				return;
			}
			final File target_file = dest.getTargetFile(name);
			System.out.println("    write to " + target_file);
			InputStream in = null;
			OutputStream out = null;
			try {
				File dir = target_file.getParentFile();
				if (!dir.exists()) {
					dir.mkdirs();
				}
				in = zip.getInputStream(ent);
				out = new FileOutputStream(target_file);
				IOTools.pump(in, out);
			} finally {
				IOTools.close(in);
				IOTools.close(out);
			}
		}
	}

}
