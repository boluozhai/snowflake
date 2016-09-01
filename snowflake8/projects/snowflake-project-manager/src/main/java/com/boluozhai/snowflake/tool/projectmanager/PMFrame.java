package com.boluozhai.snowflake.tool.projectmanager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

import com.boluozhai.snowflake.tool.projectmanager.controller.ProjectNameNormalizeController;
import com.boluozhai.snowflake.tool.projectmanager.controller.ProjectSearchController;
import com.boluozhai.snowflake.tool.projectmanager.model.ProjectInfo;
import com.boluozhai.snowflake.tool.projectmanager.model.WorkspaceContext;

public class PMFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 2311453297702772386L;
	private WorkspaceContext _context;
	private JLabel _label_working_path;
	private JList<ProjectAdapter> _list_projects;

	interface CMD {

		String browse = "ac_browse";
		String search = "ac_search";
		String normal_name = "ac_normal_name";

	}

	public PMFrame(WorkspaceContext context) {
		this._context = context;
	}

	public void init() {

		this.setSize(640, 480);
		this.setTitle("Snowflake Project Manager");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setJMenuBar(this.createMenu());

		JList<ProjectAdapter> list = new JList<ProjectAdapter>();
		JLabel label = new JLabel();
		JScrollPane sp = new JScrollPane();

		this._list_projects = list;
		this._label_working_path = label;

		sp.setViewportView(list);

		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		this.add(label, BorderLayout.NORTH);
		this.add(sp, BorderLayout.CENTER);

	}

	public void updateUI() {

		WorkspaceContext context = this._context;

		File path = context.getPath();
		if (path != null) {
			this._label_working_path.setText(path.getAbsolutePath());
		}

		DefaultListModel<ProjectAdapter> model = new DefaultListModel<ProjectAdapter>();
		List<ProjectInfo> projects = context.getProjects();
		if (projects != null) {
			for (ProjectInfo proj : projects) {
				model.addElement(new ProjectAdapter(proj));
			}
		}
		this._list_projects.setModel(model);
	}

	private JMenuBar createMenu() {

		JMenu menu = null;
		JMenuBar mb = new JMenuBar();

		menu = new JMenu("Project");
		mb.add(menu);
		this.addMenuItem(menu, "Browse", CMD.browse);
		this.addMenuItem(menu, "Search", CMD.search);
		this.addMenuItem(menu, "Normalize Names", CMD.normal_name);

		return mb;
	}

	private JMenuItem addMenuItem(JMenu menu, String title, String command) {
		JMenuItem item = new JMenuItem(title);
		item.setActionCommand(command);
		item.addActionListener(this);
		menu.add(item);
		return item;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String cmd = e.getActionCommand();

		if (cmd == null) {
			// NOP

		} else if (cmd.equals(CMD.browse)) {
			this.on_cmd_project_browse();

		} else if (cmd.equals(CMD.search)) {
			this.on_cmd_project_search();

		} else if (cmd.equals(CMD.normal_name)) {
			this.on_cmd_project_normal_name();

		} else {
			// NOP
		}

	}

	private void on_cmd_project_normal_name() {
		WorkspaceContext wc = this._context;
		ProjectNameNormalizeController ctrl = new ProjectNameNormalizeController(
				wc);
		ctrl.exe();
	}

	private void on_cmd_project_search() {
		ProjectSearchController ctrl = new ProjectSearchController(
				this._context);
		ctrl.search();
		this.updateUI();
	}

	private void on_cmd_project_browse() {

		JFileChooser fc = null;

		fc = new JFileChooser();
		fc.setDialogTitle("select workspace directory");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int result = fc.showOpenDialog(this);
		if (result != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File dir = fc.getSelectedFile();
		this._context.setPath(dir);

		this.updateUI();

	}

	public class ProjectAdapter {

		private final ProjectInfo _info;

		public ProjectAdapter(ProjectInfo info) {
			this._info = info;
		}

		public String toString() {
			return _info.getDirectory().getAbsolutePath();
		}

	}

}
