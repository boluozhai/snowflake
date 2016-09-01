package com.boluozhai.snowflake.libwebapp.resource_express;

import java.util.ArrayList;
import java.util.List;

public class WebResDescriptor {

	private String pathInProject;
	private List<WebResItem> items;

	public WebResDescriptor() {
		this.pathInProject = "src/main/webapp/lib";
		this.items = new ArrayList<WebResItem>();
	}

	public List<WebResItem> getItems() {
		return items;
	}

	public void setItems(List<WebResItem> items) {
		this.items = items;
	}

	public String getPathInProject() {
		return pathInProject;
	}

	public void setPathInProject(String pathInProject) {
		this.pathInProject = pathInProject;
	}

	public void addItem(String s) {
		List<WebResItem> its = this.getItems();
		its.add(WebResItem.parse(s));
	}

	protected void onCreate() {
	}

	public void run() {
		this.onCreate();
		WebResImporter.doImport(this);
	}

}
