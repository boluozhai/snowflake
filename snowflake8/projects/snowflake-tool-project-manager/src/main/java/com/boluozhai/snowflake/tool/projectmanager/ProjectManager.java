package com.boluozhai.snowflake.tool.projectmanager;

import com.boluozhai.snowflake.tool.projectmanager.model.WorkspaceContext;

public class ProjectManager {

	public static void main(String[] args) {
		WorkspaceContext context = new WorkspaceContext();
		PM_GUI gui = new PM_GUI(context);
		gui.start();
	}

}
