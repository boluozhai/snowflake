package com.boluozhai.snowflake.test.impl;

import java.io.File;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.support.ContextWrapper;
import com.boluozhai.snowflake.test.TestContext;

public class TestContextWrapper extends ContextWrapper implements TestContext {

	private final Object _test_target;
	private PathFinder _pathfinder;

	private static class PathFinder {

		private File working_path;
		private File template_path;
		private AppData app_data;

		public static File find_testcase_template_path(AppData ad) {

			String classpath = ad.getTargetClass().getName();
			final File cp = ad.getCodePath();
			final String reg_name = "pom.xml";
			final String path = "src/test/data/" + classpath + "/template";

			// find pom.xml
			File p = cp;
			File pom = null;
			for (; p != null; p = p.getParentFile()) {
				final File ch = new File(p, reg_name);
				if (ch.isFile() && ch.exists()) {
					pom = ch;
					break;
				}
			}

			if (pom == null) {
				return null;
			}

			return new File(pom.getParentFile(), path);

		}

		public static File find_testcase_working_path(AppData ad) {
			File base = ad.getDataSchemaPath();
			return new File(base, "test/data/working");
		}

		private static void mkdirs(File dir) {
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}

	}

	public TestContextWrapper(SnowContext context, Object test_target) {
		super(context);
		this._test_target = test_target;
	}

	private PathFinder getPathFinder() {

		PathFinder finder = this._pathfinder;
		if (finder != null) {
			return finder;
		}

		finder = new PathFinder();
		Class<?> schema = this._test_target.getClass();
		AppData ad = AppData.Helper.getInstance(this, schema);
		finder.app_data = ad;

		// working path
		finder.working_path = PathFinder.find_testcase_working_path(ad);
		PathFinder.mkdirs(finder.working_path);

		// template path
		finder.template_path = PathFinder.find_testcase_template_path(ad);
		PathFinder.mkdirs(finder.template_path);

		return finder;

	}

	@Override
	public File getWorkingPath() {
		return this.getPathFinder().working_path;
	}

	@Override
	public File getWorksTemplatePath() {
		return this.getPathFinder().template_path;
	}

	@Override
	public Object getTestTarget() {
		return this._test_target;
	}

	@Override
	public AppData getAppData() {
		return this.getPathFinder().app_data;
	}

}
