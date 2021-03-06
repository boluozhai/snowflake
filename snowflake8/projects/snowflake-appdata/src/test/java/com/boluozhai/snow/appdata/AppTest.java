package com.boluozhai.snow.appdata;

import java.io.PrintStream;

import org.junit.Test;

import com.boluozhai.snowflake.appdata.AppData;

public class AppTest {

	@Test
	public void test() {

		Class<?> schema = this.getClass();
		AppData ad = AppData.Helper.getInstance(schema);
		PrintStream out = System.out;

		out.format("AppData:\n");
		out.format("        code_path = %s\n", ad.getCodePath());
		out.format("        prop_path = %s\n", ad.getPropertiesPath());
		out.format("  data_path[base] = %s\n", ad.getDataBasePath());
		out.format("  data_path[app ] = %s\n", ad.getDataSchemaPath());
		out.format("            error = %s\n", ad.getError());

	}

}
