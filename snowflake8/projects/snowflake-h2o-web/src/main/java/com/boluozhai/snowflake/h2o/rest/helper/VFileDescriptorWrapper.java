package com.boluozhai.snowflake.h2o.rest.helper;

import java.net.URI;

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
import com.boluozhai.snowflake.xgit.site.MimeTypeRegistrar;
import com.boluozhai.snowflake.xgit.site.XGitSite;
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
		String mime = this.get_mime_type(context, file);

		this._file = file;
		this._context = context;
		this._mime_type = mime;

	}

	private String get_mime_type(SnowflakeContext context, VFile file) {
		String name = file.getName();
		XGitSite site = XGitSite.Agent.getSite(context);
		MimeTypeRegistrar type_reg = site.getMimeTypeRegistrar();
		return type_reg.getTypeNameByFileName(name);
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
