package com.boluozhai.snowflake.libwebapp.resource_express;

public abstract class WebResImporter {

	public static void doImport(WebResDescriptor desc) {
		WebResImporter imp = new InnerWebResImporter();
		imp.play(desc);
	}

	public abstract void play(WebResDescriptor desc);

}
