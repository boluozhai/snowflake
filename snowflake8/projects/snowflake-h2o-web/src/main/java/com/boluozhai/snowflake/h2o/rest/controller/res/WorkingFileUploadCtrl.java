package com.boluozhai.snowflake.h2o.rest.controller.res;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.h2o.data.H2oDataTable;
import com.boluozhai.snowflake.h2o.data.dao.AliasDAO;
import com.boluozhai.snowflake.h2o.data.dao.RepoDAO;
import com.boluozhai.snowflake.h2o.data.pojo.element.RepoItem;
import com.boluozhai.snowflake.libwebapp.utils.WebContextUtils;
import com.boluozhai.snowflake.rest.path.PathPart;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.vfs.FileWorkspace;

@MultipartConfig
public class WorkingFileUploadCtrl extends RestController {

	private final WorkingFileDownloadCtrl down;

	public WorkingFileUploadCtrl() {
		this.down = new WorkingFileDownloadCtrl();
	}

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.down.rest_get(request, response);
	}

	@Override
	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		MyUpLoader loader = new MyUpLoader(request);
		loader.preload();
		loader.load();
		loader.send(response);
	}

	private static class MyOutputStreamWrapper extends OutputStream {

		private final VFile _file;
		private final VFile _file_tmp;
		private final long _reg_size;
		private OutputStream _out;

		public MyOutputStreamWrapper(VFile file, VFSIO vfsio, long size)
				throws IOException {
			this._file = file;
			this._reg_size = size;
			this._file_tmp = make_temp_file(file);
			this._out = vfsio.output(this._file_tmp);
		}

		private static VFile make_temp_file(VFile file) {
			VFile dir = file.getParentFile();
			return dir.child(file.getName() + "~");
		}

		@Override
		public void write(int b) throws IOException {
			_out.write(b);
		}

		@Override
		public void write(byte[] data) throws IOException {
			_out.write(data);
		}

		@Override
		public void write(byte[] data, int offset, int length)
				throws IOException {
			_out.write(data, offset, length);
		}

		@Override
		public void close() throws IOException {
			try {
				_out.close();
			} finally {

				if (this._file.exists()) {
					String msg = "the target file is exists: ";
					throw new IOException(msg + _file);
				}

				if (this._file_tmp.length() == this._reg_size) {
					// save
					this._file_tmp.renameTo(_file);
				} else {
					this._file_tmp.delete();
					String msg = "bad size of uploaded file.";
					throw new IOException(msg);
				}

			}
		}

	}

	private static class MyUpLoader {

		private final HttpServletRequest request;
		private final VFSIO vfsio;
		private final SnowflakeContext context;
		private VFile dir;

		public MyUpLoader(HttpServletRequest request) {
			SnowflakeContext context = WebContextUtils.getWebContext(request);
			this.vfsio = VFSIO.Agent.getInstance(context);
			this.request = request;
			this.context = context;
		}

		public void preload() {

			String uid = this.getRequiredParam("uid");
			String repo_id = this.getRequiredParam("repository");
			String path = this.getRequiredParam("path");

			DataClient client = null;

			try {

				client = H2oDataTable.openClient(context);

				AliasDAO alias_dao = new AliasDAO(client);
				RepoDAO repo_dao = new RepoDAO(client);

				uid = alias_dao.findUser(uid);
				final RepoItem repo_item = repo_dao.findRepo(uid, repo_id);

				URI uri = URI.create(repo_item.getLocation());
				RepositoryManager rm = XGit.getRepositoryManager(context);
				Repository repo = rm.open(context, uri, null);
				FileWorkspace works = (FileWorkspace) repo.getWorking();
				VFile wkdir = works.getFile();

				this.dir = this.calc_path(wkdir, path);

			} finally {
				IOTools.close(client);
			}

		}

		private VFile calc_path(VFile wkdir, String path) {
			PathPart pp = PathPart.parse(path);
			pp = pp.trim();
			String[] array = pp.toArray();
			VFile p = wkdir;
			for (String s : array) {
				p = p.child(s);
			}
			return p;
		}

		private String getRequiredParam(String key) {
			String value = request.getParameter(key);
			if (value == null) {
				throw new RuntimeException("need for param: " + key);
			}
			return value;
		}

		public void send(HttpServletResponse response) throws ServletException,
				IOException {
			JsonRestView view = new JsonRestView();
			view.setResponsePOJO("OK");
			view.handle(request, response);
		}

		public void load() throws IOException, ServletException {
			InputStream in = null;
			OutputStream out = null;
			try {
				request.setCharacterEncoding("utf-8");
				Collection<Part> parts = request.getParts();
				Iterator<Part> iter = parts.iterator();
				for (; iter.hasNext();) {
					final Part part = iter.next();
					out = this.openOutput(part);
					if (out != null) {
						in = part.getInputStream();
						IOTools.pump(in, out);
						out.close();
						out = null;
						in.close();
						in = null;
					}
				}
			} finally {
				IOTools.close(in);
				IOTools.close(out);
			}
		}

		private OutputStream openOutput(Part part) throws IOException {

			Map<String, String> map = this.load_content_disp(part);
			final String filename = map.get("filename");
			final String type = part.getContentType();
			final long size = part.getSize();

			if (filename == null) {
				return null;
			}

			// log
			String txt = "[Part filename:%s, size:%d, type:%s]";
			txt = String.format(txt, filename, size, type);
			System.out.println(txt);

			// make file
			VFile file = null;
			for (int itry = 0; itry < 100; itry++) {
				final String qname;
				if (itry == 0) {
					qname = filename;
				} else {
					final int j = filename.lastIndexOf('.');
					if (j < 0) {
						qname = String.format("%s[%d]", filename, itry);
					} else {
						String s1 = filename.substring(0, j);
						String s2 = filename.substring(j);
						qname = String.format("%s[%d]%s", s1, itry, s2);
					}
				}
				file = this.dir.child(qname);
				if (file.exists()) {
					continue;
				} else {
					break;
				}
			}

			return new MyOutputStreamWrapper(file, this.vfsio, size);

		}

		private Map<String, String> load_content_disp(Part part) {
			final Map<String, String> map = new HashMap<String, String>();
			final String cd_key = "content-disposition";
			final String cd = part.getHeader(cd_key);
			final String[] array = cd.split(";");
			for (String s : array) {
				int i = s.indexOf('=');
				if (i < 0) {
					map.put(s, "");
				} else {
					String k = s.substring(0, i).trim();
					String v = s.substring(i + 1).trim();
					v = this.remove_ref_mark(v);
					map.put(k, v);
				}
			}
			System.out.println(map);
			return map;
		}

		private String remove_ref_mark(String v) {
			final String mark = String.valueOf('"');
			if (v == null) {
				return v;
			} else if (v.startsWith(mark) && v.endsWith(mark)) {
				return v.substring(1, v.length() - 1);
			} else {
				return v;
			}
		}

	}

}
