package com.boluozhai.snowflake.h2o.rest.view;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.api.h2o.FileModel;
import com.boluozhai.snowflake.rest.element.file.Node;
import com.boluozhai.snowflake.rest.element.file.NodeList;
import com.boluozhai.snowflake.rest.path.PathPart;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestView;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;

public class FolderView extends RestView {

	private PathPart offset;
	private VPath base;
	private boolean baseAtFsRoot;

	public boolean isBaseAtFsRoot() {
		return baseAtFsRoot;
	}

	public void setBaseAtFsRoot(boolean baseAtFsRoot) {
		this.baseAtFsRoot = baseAtFsRoot;
	}

	public PathPart getOffset() {
		return offset;
	}

	public void setOffset(PathPart offset) {
		this.offset = offset;
	}

	public VPath getBase() {
		return base;
	}

	public void setBase(VPath base) {
		this.base = base;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		NodeListBuilder nlb = new NodeListBuilder();
		nlb.base = this.base;
		nlb.offset = this.offset;
		nlb.uri = request.getRequestURL().toString();
		nlb.baseAtFsRoot = this.baseAtFsRoot;

		FileModel model = new FileModel();
		model.setVfile(nlb.create());

		JsonRestView view = new JsonRestView();
		view.setResponsePOJO(model);
		view.handle(request, response);

	}

	private static class NodeListBuilder {

		public PathPart offset;
		public VPath base;
		public String uri;
		public boolean baseAtFsRoot;

		public NodeList create() {

			if (this.is_windows_root()) {
				return this.create4windowsRoot();
			}

			final VFile node = this.getThisNode();
			final List<Node> list = this.getChildList(node);
			final NodeList nlist = new NodeList();

			nlist.setExists(node.exists());
			nlist.setLastModified(node.lastModified());
			nlist.setName(node.getName());
			nlist.setLength(node.length());
			nlist.setDirectory(node.isDirectory());
			nlist.setFileURI(node.toURI().toString());
			nlist.setBaseURI(this.makeBaseURI(nlist));
			nlist.setPath(this.offset.toArray());
			nlist.setList(list);

			nlist.setDebugAbsPath(node.getAbsolutePath());
			nlist.setDebugURI(this.uri);

			return nlist;
		}

		private boolean is_windows_root() {

			VFS vfs = this.base.file().vfs();
			char sep = vfs.separatorChar();
			int len = this.offset.length;
			boolean at_root = this.baseAtFsRoot;

			return ((at_root) && (len == 0) && (sep == '\\'));
		}

		private String makeBaseURI(NodeList nlist) {
			final String full = nlist.getFileURI();
			final String path = this.offset.toString();
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

			VFS vfs = this.base.file().vfs();
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

			// TODO fix bug of path name in Chinese

			final VFile file = this.base.file();
			final String off = this.offset.toString();
			final String base;
			if (this.baseAtFsRoot) {
				base = "file:///";
			} else {
				base = file.toURI().toString();
			}
			String url;
			if (base.endsWith("/")) {
				url = base + off;
			} else {
				url = base + "/" + off;
			}
			if (!url.endsWith("/")) {
				url = url + '/';
			}
			final VFS vfs = file.vfs();
			return vfs.newFile(URI.create(url));
		}

	}

}
