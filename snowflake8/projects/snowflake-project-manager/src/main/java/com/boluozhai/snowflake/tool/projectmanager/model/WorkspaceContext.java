package com.boluozhai.snowflake.tool.projectmanager.model;

import java.io.File;
import java.util.List;

public class WorkspaceContext {

	private File path;
	private List<ProjectInfo> projects;

	public File getPath() {
		return path;
	}

	public void setPath(File path) {
		this.path = path;
	}

	public List<ProjectInfo> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectInfo> projects) {
		this.projects = projects;
	}

}
