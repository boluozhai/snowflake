package com.boluozhai.snowflake.tool.projectmanager.model;

import java.io.File;

public class ProjectInfo {

	private String name;
	private File directory;
	private File pomXmlFile;
	private File eclipseProjectFile;

	public ProjectInfo(WorkspaceContext context) {
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public File getPomXmlFile() {
		return pomXmlFile;
	}

	public void setPomXmlFile(File pomXmlFile) {
		this.pomXmlFile = pomXmlFile;
	}

	public File getEclipseProjectFile() {
		return eclipseProjectFile;
	}

	public void setEclipseProjectFile(File eclipseProjectFile) {
		this.eclipseProjectFile = eclipseProjectFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
