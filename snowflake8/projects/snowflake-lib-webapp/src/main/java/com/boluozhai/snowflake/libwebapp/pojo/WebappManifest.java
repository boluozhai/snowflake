package com.boluozhai.snowflake.libwebapp.pojo;

import java.util.List;

public class WebappManifest {

	private String scheme; // must be 'com.boluozhai.snowflake'
	private String name; // the name of '{name}.war'
	private String namespace;
	private String title;// the name for human read
	private String rest; // the path of restful API

	private List<String> rest_classes;

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRest() {
		return rest;
	}

	public void setRest(String rest) {
		this.rest = rest;
	}

	public List<String> getRest_classes() {
		return rest_classes;
	}

	public void setRest_classes(List<String> rest_classes) {
		this.rest_classes = rest_classes;
	}

}
