package com.boluozhai.snowflake.rest.server.support;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.rest.path.PathPart;
import com.boluozhai.snowflake.rest.path.PathPattern;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;

public class DefaultPathInfo implements PathInfo {

	private PathPart _full_part;
	private PathPart _context_part;
	private PathPart _in_app_part;
	private Map<String, PathPart> _named_parts;

	public DefaultPathInfo(HttpServletRequest request, PathPattern pattern) {
		BasePathParser parser = new BasePathParser();
		parser.parse(request);
		this._full_part = parser.full;
		this._context_part = parser.context;
		this._in_app_part = parser.in_app;
		this._named_parts = parser.make_named_parts(pattern);
	}

	@Override
	public PathPart getFullPart() {
		return this._full_part;
	}

	@Override
	public PathPart getContextPart() {
		return this._context_part;
	}

	@Override
	public PathPart getInAppPart() {
		return this._in_app_part;
	}

	private static class BasePathParser {

		public PathPart in_app;
		public PathPart context;
		public PathPart full;

		public void parse(HttpServletRequest request) {

			String context_path = request.getContextPath();
			URI uri = URI.create(request.getRequestURI());
			String full_path = uri.getPath();

			this.full = this.make_part(full_path);
			this.context = this.make_part(context_path);
			this.in_app = this.make_in_app_part();

		}

		public Map<String, PathPart> make_named_parts(PathPattern pattern) {

			PathPart[] pps = pattern.getParts();

			final PathPart inapp = this.in_app;
			final Map<String, PathPart> map = new HashMap<String, PathPart>();
			int count = 0;
			for (PathPart pp1 : pps) {
				String name = pp1.data[pp1.offset];
				if (pp1.length == 0) {
					// the end
					final String[] d2 = inapp.data;
					final int o2 = inapp.offset + count;
					final int l2 = inapp.length - count;
					final PathPart pp2 = new PathPart(d2, o2, l2);
					map.put(name, pp2);
					break;
				}
				final String[] d2 = inapp.data;
				final int o2 = inapp.offset + count;
				final int l2 = pp1.length;
				final PathPart pp2 = new PathPart(d2, o2, l2);
				map.put(name, pp2);
				count += pp1.length;
			}

			return map;
		}

		private PathPart make_in_app_part() {
			String[] data = this.full.data;
			int offset = this.context.length;
			int length = this.full.length - this.context.length;
			return new PathPart(data, offset, length);
		}

		private PathPart make_part(String str) {
			str = str.replace('\\', '/');
			String[] array = str.split("/");
			List<String> list = new ArrayList<String>();
			for (String s : array) {
				if (s == null) {
					continue;
				} else {
					s = s.trim();
				}
				if (s.length() == 0) {
					continue;
				} else if (s.equals(".")) {
					continue;
				} else if (s.equals("..")) {
					list.remove(list.size() - 1);
				} else {
					list.add(s);
				}
			}
			array = list.toArray(new String[list.size()]);
			return new PathPart(array, 0, array.length);
		}

	}

	@Override
	public PathPart getPart(String name) {
		return this.getPart(name, false);
	}

	@Override
	public PathPart getRequiredPart(String name) {
		return this.getPart(name, true);
	}

	@Override
	public PathPart getPart(String name, boolean required) {
		PathPart pp = this._named_parts.get(name);
		if (pp == null) {
			if (required) {
				throw new SnowflakeException("no required path-part: " + name);
			}
		}
		return pp;
	}

	@Override
	public String getPartString(String name) {
		return this.getPartString(name, false);
	}

	@Override
	public String getPartString(String name, boolean required) {

		PathPart p = this.getPart(name, required);
		if (p == null) {
			if (required) {
				throw new SnowflakeException("no required path-part: " + name);
			}
			return null;
		} else {
			return p.toString();
		}

	}

}
