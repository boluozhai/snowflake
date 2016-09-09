package com.boluozhai.snowflake.rest.impl.client;

import java.net.URI;

final class RestUriTools {

	public static URI child(URI base, String offset) {
		return child(base, offset, true);
	}

	public static URI child(URI base, String offset, boolean simple_name_only) {

		String[] a2 = split2array(offset);
		if (simple_name_only) {
			if (a2.length > 1) {
				String msg = "the offset [%s] must be a simple name.";
				msg = String.format(msg, offset);
				throw new RuntimeException(msg);
			}
		}
		String[] a1 = split2array(base.getPath());

		UriMaker maker = new UriMaker();
		maker.set(base);
		maker.resetPath();
		maker.appendPath(a1);
		maker.appendPath(a2);
		return maker.create();
	}

	private static String[] split2array(String str) {
		str = str.replace('\\', '/');
		return str.split("/");
	}

	private static class UriMaker {

		private String _scheme;
		private String _user;
		private String _host;
		private int _port;
		private StringBuilder _path;
		private String _query;

		public void set(URI base) {

			String path = base.getPath();
			StringBuilder sb = new StringBuilder();
			if (path != null) {
				sb.append(path);
			}

			this._scheme = base.getScheme();
			this._user = base.getUserInfo();
			this._host = base.getHost();
			this._port = base.getPort();
			this._path = sb;
			this._query = base.getQuery();
		}

		public URI create() {

			StringBuilder sb = new StringBuilder();

			if (_scheme != null) {
				sb.append(_scheme).append(':');
			}

			boolean has_u = (_user != null);
			boolean has_h = (_host != null);
			boolean has_p = (_port > 0);

			if (has_h || has_p || has_u) {
				sb.append("//");
			}
			if (has_u) {
				sb.append(_user).append('@');
			}
			if (has_h) {
				sb.append(_host);
			}
			if (has_p) {
				sb.append(':').append(_port);
			}

			sb.append(_path.toString());

			if (_query != null) {
				sb.append('?').append(_query);
			}

			return URI.create(sb.toString());
		}

		public void appendPath(String[] array) {
			StringBuilder sb = this._path;
			for (String s : array) {
				if (s == null) {
					continue;
				}
				if (s.length() == 0) {
					continue;
				}
				sb.append('/').append(s);
			}
		}

		public void resetPath() {
			this._path.setLength(0);
		}
	}

}
