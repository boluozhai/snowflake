package com.boluozhai.snowflake.h2o.tools;

import com.boluozhai.snowflake.libwebapp.resource_express.WebResDescriptor;

public class ResImport extends WebResDescriptor {

	public static void main(String[] arg) {

		ResImport ri = new ResImport();

		ri.addItem("com.boluozhai.snowflake:snowflake-web-resources-war:0.8.0:lib");

		ri.run();

	}

}
