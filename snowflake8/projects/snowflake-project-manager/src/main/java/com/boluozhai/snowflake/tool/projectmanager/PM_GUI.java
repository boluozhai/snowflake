package com.boluozhai.snowflake.tool.projectmanager;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.boluozhai.snowflake.tool.projectmanager.model.WorkspaceContext;

public class PM_GUI implements Runnable {

	private WorkspaceContext _context;

	public PM_GUI(WorkspaceContext context) {
		this._context = context;
	}

	public void start() {
		try {
			SwingUtilities.invokeAndWait(this);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		PMFrame frame = new PMFrame(this._context);
		frame.init();
		frame.setVisible(true);
	}

}
