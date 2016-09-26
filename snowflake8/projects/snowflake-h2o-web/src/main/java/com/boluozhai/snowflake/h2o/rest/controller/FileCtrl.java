package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.h2o.rest.helper.H2oRestInfo;
import com.boluozhai.snowflake.h2o.utils.PathElements2VFile;
import com.boluozhai.snowflake.rest.api.h2o.FileModel;
import com.boluozhai.snowflake.rest.element.file.Node;
import com.boluozhai.snowflake.rest.element.file.NodeList;
import com.boluozhai.snowflake.rest.path.PathPart;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;

public class FileCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		JsonRestView view = new JsonRestView();
		try {

			// context
			H2oRestInfo rest_info = H2oRestInfo.getInstance(request);
			SnowflakeContext context = rest_info.getRequestInfo().getContext();

			// make model
			FileModel model = new FileModel();
			VFS vfs = VFS.Factory.getVFS(context);
			NodeListBuilder nlb = new NodeListBuilder();
			nlb.vfs = vfs;
			nlb.offset_path = rest_info.getId();
			nlb.uri = request.getRequestURL().toString();
			view.setResponsePOJO(model);
			model.setVfile(nlb.create());

		} finally {
			view.handle(request, response);
		}

	}

	private static class NodeListBuilder {

		public PathPart offset_path;
		public VFS vfs;
		private String uri;

		public NodeList create() {

			final VFile node = this.getThisNode();
			if (node == null) {
				return this.create4windowsRoot();
			}

			final List<Node> list = this.getChildList(node);
			final NodeList nlist = new NodeList();

			nlist.setExists(node.exists());
			nlist.setLastModified(node.lastModified());
			nlist.setName(node.getName());
			nlist.setLength(node.length());
			nlist.setDirectory(node.isDirectory());
			nlist.setFileURI(node.toURI().toString());
			nlist.setBaseURI(this.makeBaseURI(nlist));
			nlist.setPath(this.offset_path.toArray());
			nlist.setList(list);

			nlist.setDebugAbsPath(node.getAbsolutePath());
			nlist.setDebugURI(this.uri);

			return nlist;
		}

		private String makeBaseURI(NodeList nlist) {
			final String full = nlist.getFileURI();
			final String[] array = this.offset_path.toArray();
			final StringBuilder sb = new StringBuilder();
			for (String s : array) {
				if (sb.length() > 0) {
					sb.append('/');
				}
				sb.append(s);
			}
			final String path = sb.toString();
			final int index = full.lastIndexOf(path);
			if (index < 0) {
				return full;
			} else {
				return full.substring(0, index);
			}
		}

		private NodeList create4windowsRoot() {

			NodeList nlist = new NodeList();
			List<Node> list = new ArrayList<Node>();

			VFile[] roots = vfs.listRoots();
			for (VFile drv : roots) {
				Node node = new Node();
				String name = drv.getAbsolutePath().replace('\\', ' ').trim();
				node.setName(name);
				node.setDirectory(true);
				list.add(node);
			}

			String[] path = {};

			nlist.setPath(path);
			nlist.setList(list);
			nlist.setExists(true);
			nlist.setDirectory(true);
			nlist.setDebugAbsPath("\\");
			nlist.setDebugURI(this.uri);
			nlist.setBaseURI("file:/");
			nlist.setFileURI("file:/");

			return nlist;
		}

		private List<Node> getChildList(VFile node) {
			if (node == null) {
				return null;
			}
			String[] list = node.list();
			if (list == null) {
				return null;
			}
			List<Node> li = new ArrayList<Node>();
			for (String name : list) {
				VFile chf = node.child(name);
				Node chn = new Node();
				chn.setName(name);
				chn.setDirectory(chf.isDirectory());
				chn.setLastModified(chf.lastModified());
				chn.setLength(chf.length());
				li.add(chn);
			}
			return li;
		}

		private VFile getThisNode() {
			PathElements2VFile tools = new PathElements2VFile(vfs);
			return tools.getFile(offset_path.toArray());
		}

	}

}
