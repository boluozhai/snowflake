package com.boluozhai.snowflake.h2o.rest.helper;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.h2o.data.H2oDataTable;
import com.boluozhai.snowflake.h2o.data.dao.AliasDAO;
import com.boluozhai.snowflake.h2o.data.dao.RepoDAO;
import com.boluozhai.snowflake.h2o.data.pojo.element.RepoItem;
import com.boluozhai.snowflake.libwebapp.utils.WebContextUtils;
import com.boluozhai.snowflake.rest.path.PathPart;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.vfs.FileWorkspace;

public class VFileDescriptorWrapper {

	private final VFileDescriptor descriptor;
	private final HttpServletRequest request;
	private String _mime_type;
	private VFile _file;
	private SnowflakeContext _context;

	private VFileDescriptorWrapper(VFileDescriptor descriptor,
			HttpServletRequest request) {
		this.descriptor = descriptor;
		this.request = request;
	}

	public static VFileDescriptorWrapper create(HttpServletRequest request) {

		String owner = request.getParameter("owner");
		String repo = request.getParameter("repository");
		String type = request.getParameter("type");
		String id = request.getParameter("id");
		String base = request.getParameter("base");
		String offset = request.getParameter("offset");

		VFileDescriptor inner = new VFileDescriptor();
		inner.setBase(base);
		inner.setId(id);
		inner.setOffset(offset);
		inner.setOwner(owner);
		inner.setRepository(repo);
		inner.setType(type);

		return new VFileDescriptorWrapper(inner, request);

	}

	public VFileDescriptor getDescriptor() {
		return descriptor;

	}

	public VFile getFile() {
		return this._file;
	}

	public String getMimeType() {
		return this._mime_type;
	}

	public void load() {

		final VFileDescriptor desc = this.descriptor;
		final SnowflakeContext context = WebContextUtils.getWebContext(request);

		// query data-table
		DataClient client = null;
		URI uri = null;
		try {
			client = H2oDataTable.openClient(context);
			AliasDAO alias_dao = new AliasDAO(client);
			RepoDAO repo_dao = new RepoDAO(client);
			String uid = desc.getOwner();
			uid = alias_dao.findUser(uid);
			RepoItem repo = repo_dao.findRepo(uid, desc.getRepository());
			uri = URI.create(repo.getLocation());
		} finally {
			IOTools.close(client);
		}

		// open repo
		RepositoryManager rm = XGit.getRepositoryManager(context);
		Repository repo = rm.open(context, uri, null);
		FileWorkspace works = (FileWorkspace) repo.getWorking();
		VFile wkdir = works.getFile();

		// make path
		VFile file = this.make_path(wkdir, desc);

		// mime type
		String mime = this.get_mime_type(file);

		this._file = file;
		this._context = context;
		this._mime_type = mime;

	}

	private static Map<String, String> mime_types_table;

	private String get_mime_type(VFile file) {

		final String name = file.getName().toLowerCase();
		final int idot = name.lastIndexOf('.');
		final String suffix;
		if (idot < 0) {
			suffix = "";
		} else {
			suffix = name.substring(idot);
		}

		Map<String, String> map = mime_types_table;
		if (map == null) {
			map = new HashMap<String, String>();
			this.init_mime_types_table(map);
			map = Collections.synchronizedMap(map);
			mime_types_table = map;
		}

		String mime = map.get(suffix);
		if (mime == null) {
			mime = map.get("");
		}
		return mime;
	}

	private void init_mime_types_table(Map<String, String> map) {

		// TODO

		map.put(".txt", "text/plain");
		map.put(".html", "text/html");
		map.put(".js", "application/javascript");
		map.put(".css", "text/css");

		map.put(".bmp", "image/bmp");
		map.put(".png", "image/png");
		map.put(".jpg", "image/jpeg");
		map.put(".gif", "image/gif");

		map.put(".mp3", "audio/mp3");
		map.put(".mp4", "video/mpeg4");

		map.put("", "octet-stream");

	}

	private VFile make_path(VFile wkdir, VFileDescriptor desc) {

		String base = desc.getBase();
		String offset = desc.getOffset();

		PathPart pp1 = PathPart.parse(base);
		PathPart pp2 = PathPart.parse(offset);

		String[] a1 = pp1.trimToArray();
		String[] a2 = pp2.trimToArray();

		VFile p = wkdir;
		for (String s : a1) {
			p = p.child(s);
		}
		for (String s : a2) {
			p = p.child(s);
		}
		return p;
	}

	public SnowflakeContext getContext() {
		return this._context;
	}

}
