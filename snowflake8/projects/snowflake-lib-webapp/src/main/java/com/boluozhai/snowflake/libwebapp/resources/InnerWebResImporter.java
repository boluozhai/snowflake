package com.boluozhai.snowflake.libwebapp.resources;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;

final class InnerWebResImporter extends WebResImporter {

	@Override
	public void play(WebResDescriptor desc) {

		// TODO Auto-generated method stub

		Task task = new Task(desc);

		Dest dest = new Dest();
		dest.load(desc);

		List<WebResItem> items = desc.getItems();
		for (WebResItem it : items) {
			Src src = new Src(task);
			src.load(it);
			src.writeTo(dest);
		}

	}

	private class Task {

		private File _m2_dir;
		private SnowflakeContext _context;

		public Task(WebResDescriptor desc) {
			// TODO Auto-generated constructor stub
		}

		public File getWar(WebResItem item) {
			// TODO Auto-generated method stub

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

		public void load(WebResDescriptor desc) {

			try {
				final URL loc = desc.getClass().getProtectionDomain()
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

				String path = desc.getPathInProject();

				this._pom_xml = p;
				this._target_dir = new File(p.getParentFile(), path);

			} catch (URISyntaxException e) {

				throw new RuntimeException(e);

			} finally {

				System.out.println("to " + this._target_dir);

			}
		}
	}

	private class Src {

		private final Task _task;

		public Src(Task task) {
			this._task = task;
		}

		public void load(WebResItem item) {
			// TODO Auto-generated method stub

			// location war
			File war = _task.getWar(item);

			System.out.println("load " + war);

		}

		public void writeTo(Dest desc) {
			// TODO Auto-generated method stub

		}
	}

}
