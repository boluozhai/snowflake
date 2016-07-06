package com.boluozhai.snowflake.tool.projectmanager.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.tool.projectmanager.model.ProjectInfo;
import com.boluozhai.snowflake.tool.projectmanager.model.WorkspaceContext;

public class ProjectSearchController {

	private final WorkspaceContext _context;

	public ProjectSearchController(WorkspaceContext wc) {
		this._context = wc;
	}

	public void search() {
		WorkspaceContext wc = this._context;
		File dir = wc.getPath();
		SearchContext sc = new SearchContext(wc);
		this.search(sc, dir, 3);
		List<ProjectInfo> projects = sc.makeResult();
		wc.setProjects(projects);
	}

	private void search(SearchContext context, File dir, int depthLimit) {

		if (depthLimit < 0) {
			return;
		}

		if (!dir.isDirectory()) {
			return;
		}

		File pom = new File(dir, "pom.xml");
		File project = new File(dir, ".project");
		if (pom.exists() && project.exists()) {
			context.addProject(pom, project);
		} else {
			File[] chs = dir.listFiles();
			for (File ch : chs) {
				this.search(context, ch, depthLimit - 1);
			}
		}

	}

	private class SearchContext {

		Map<String, ProjectInfo> projects = new HashMap<String, ProjectInfo>();
		private final WorkspaceContext _working_context;

		public SearchContext(WorkspaceContext context) {
			this._working_context = context;
		}

		public List<ProjectInfo> makeResult() {

			List<ProjectInfo> list = new ArrayList<ProjectInfo>();
			Map<String, ProjectInfo> map = projects;

			List<String> keys = new ArrayList<String>(map.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				list.add(map.get(key));
			}

			return list;

		}

		public void addProject(File pom, File eclipseProject) {

			File dir = pom.getParentFile();
			String name = dir.getName();

			ProjectInfo info = new ProjectInfo(_working_context);
			info.setName(name);
			info.setDirectory(dir);
			info.setPomXmlFile(pom);
			info.setEclipseProjectFile(eclipseProject);

			projects.put(name, info);

		}
	}

}
