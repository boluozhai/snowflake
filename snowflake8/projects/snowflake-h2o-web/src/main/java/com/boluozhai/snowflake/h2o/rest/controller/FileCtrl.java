package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.h2o.utils.PathElements2VFile;
import com.boluozhai.snowflake.libwebapp.utils.WebContextUtils;
import com.boluozhai.snowflake.rest.api.h2o.FileModel;
import com.boluozhai.snowflake.rest.element.file.Node;
import com.boluozhai.snowflake.rest.element.file.NodeList;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.RestServlet.RestInfo;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;

public class FileCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JsonRestView view = new JsonRestView();
		try {

			// context
			ServletContext sc = request.getServletContext();
			SnowflakeContext context = WebContextUtils.getWebContext(sc);
			RestInfo rest_info = this.getRestInfo(request);

			// make model
			FileModel model = new FileModel();
			VFS vfs = VFS.Factory.getVFS(context);
			NodeListBuilder nlb = new NodeListBuilder();
			nlb.vfs = vfs;
			nlb.rest_info = rest_info;
			nlb.uri = request.getRequestURL().toString();
			view.setResponsePOJO(model);
			model.setVfile(nlb.create());

		} finally {
			view.forward(request, response);
		}

	}

	private static class NodeListBuilder {

		public RestInfo rest_info;
		public VFS vfs;
		private String uri;

		public NodeList create() {

			if (rest_info.id.length == 0) {
				if (vfs.separatorChar() == '\\') {
					return this.create4windowsRoot();
				}
			}

			final VFile node = this.getThisNode();
			final List<Node> list = this.getChildList(node);
			final NodeList nlist = new NodeList();

			if (node != null) {
				nlist.setExists(node.exists());
				nlist.setLastModified(node.lastModified());
				nlist.setName(node.getName());
				nlist.setLength(node.length());
				nlist.setDirectory(node.isDirectory());
			}

			nlist.setDebugAbsPath(node.getAbsolutePath());
			nlist.setDebugURI(this.uri);
			nlist.setPath(rest_info.id);
			nlist.setList(list);

			return nlist;
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

			nlist.setPath(rest_info.id);
			nlist.setList(list);
			nlist.setExists(true);
			nlist.setDirectory(true);
			nlist.setDebugAbsPath("\\");
			nlist.setDebugURI(this.uri);

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
			return tools.getFile(rest_info.id);
		}

	}

}
