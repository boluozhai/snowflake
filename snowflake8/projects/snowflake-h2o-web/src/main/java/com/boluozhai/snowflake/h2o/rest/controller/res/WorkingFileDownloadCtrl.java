package com.boluozhai.snowflake.h2o.rest.controller.res;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.h2o.rest.helper.VFileDescriptorWrapper;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;

public class WorkingFileDownloadCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		MyLoader loader = new MyLoader(request);
		loader.load();
		loader.send(response);

	}

	private static class MyLoader {

		private final HttpServletRequest request;
		private SnowflakeContext context;
		private VFSIO vfsio;
		private VFile file;
		private String mime_type;

		public MyLoader(HttpServletRequest request) {
			this.request = request;
		}

		public void send(HttpServletResponse response) throws IOException {

			if (!file.exists()) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getOutputStream().print("http 404 not_found");
				return;
			} else if (file.isDirectory()) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getOutputStream().print("http 403 forbidden");
				return;
			}

			final long length = file.length();
			if (length > Integer.MAX_VALUE) {
				throw new IOException("the file is too large, cb: " + length);
			}

			response.setStatus(HttpServletResponse.SC_OK);
			response.setCharacterEncoding("UTF-8");
			response.setContentType(this.mime_type);
			response.setContentLength((int) length);

			InputStream in = null;
			OutputStream out = null;

			try {
				in = vfsio.input(file);
				out = response.getOutputStream();
				IOTools.pump(in, out);
			} finally {
				IOTools.close(in);
				IOTools.close(out);
			}

		}

		public void load() {

			VFileDescriptorWrapper vfdw = VFileDescriptorWrapper
					.create(request);

			vfdw.load();

			this.context = vfdw.getContext();
			this.file = vfdw.getFile();
			this.mime_type = vfdw.getMimeType();
			this.vfsio = VFSIO.Agent.getInstance(context);

		}

	}

}
