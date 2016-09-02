package com.boluozhai.snowflake.rest.element.file;

import java.util.List;

public class NodeList extends Node {

	private String[] path;
	private List<Node> list;
	private boolean exists;

	private String debugBaseURI;
	private String debugURI; // =(baseURI+path)
	private String debugAbsPath;

	public List<Node> getList() {
		return list;
	}

	public void setList(List<Node> list) {
		this.list = list;
	}

	public String[] getPath() {
		return path;
	}

	public void setPath(String[] path) {
		this.path = path;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public String getDebugURI() {
		return debugURI;
	}

	public void setDebugURI(String debugURI) {
		this.debugURI = debugURI;
	}

	public String getDebugAbsPath() {
		return debugAbsPath;
	}

	public void setDebugAbsPath(String debugAbsPath) {
		this.debugAbsPath = debugAbsPath;
	}

	public String getDebugBaseURI() {
		return debugBaseURI;
	}

	public void setDebugBaseURI(String debugBaseURI) {
		this.debugBaseURI = debugBaseURI;
	}

}
