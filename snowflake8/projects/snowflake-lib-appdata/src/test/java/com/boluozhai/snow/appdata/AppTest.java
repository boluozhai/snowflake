package com.boluozhai.snow.appdata;

import java.io.PrintStream;

import org.junit.Test;

public class AppTest {

	@Test
	public void test() {

		AppData ad = AppData.Helper.getInstance();
		PrintStream out = System.out;

		out.format("AppData:\n");
		out.format("        code_path = %s\n", ad.getCodePath());
		out.format("        prop_path = %s\n", ad.getPropertiesPath());
		out.format("  data_path[base] = %s\n", ad.getDataBasePath());
		out.format("  data_path[app ] = %s\n", ad.getDataPath(this.getClass()));
		out.format("            error = %s\n", ad.getError());

	}

}
