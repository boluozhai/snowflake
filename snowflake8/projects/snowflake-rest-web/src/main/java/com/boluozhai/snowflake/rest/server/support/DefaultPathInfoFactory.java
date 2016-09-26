package com.boluozhai.snowflake.rest.server.support;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfoFactory;
import com.boluozhai.snowflake.rest.server.info.path.PathPart;

public class DefaultPathInfoFactory implements PathInfoFactory {

	private String inAppPathPattern;
	private PathPart[] _pattern;

	public String getInAppPathPattern() {
		return inAppPathPattern;
	}

	public void setInAppPathPattern(String inAppPathPattern) {
		this.inAppPathPattern = inAppPathPattern;
	}

	@Override
	public PathInfo create(HttpServletRequest request) {

		PathPart[] pattern = this._pattern;
		if (pattern == null) {
			pattern = PatternMaker.parse(this.inAppPathPattern);
			this._pattern = pattern;
		}

		return new DefaultPathInfo(request, pattern);
	}

	private static class PatternMaker {

		// sample pattern string : '~/app/api/?/?/type/?/?/?/id/*'

		public static PathPart[] parse(String pattern) {
			PatternMaker mk = new PatternMaker();
			String[] array = pattern.split("/");
			for (String s : array) {
				mk.append(s);
			}
			mk.flush();
			return mk.create();
		}

		private String _cur_name;
		private int _cur_len;
		private List<PathPart> _pplist = new ArrayList<PathPart>();

		private PathPart[] create() {
			List<PathPart> list = this._pplist;
			return list.toArray(new PathPart[list.size()]);
		}

		private void append(String s) {

			s = s.trim();
			if (s.length() == 0) {
				return;
			} else if (s.equals("~")) {
				return;
			} else if (s.equals("?")) {
				this._cur_len++;
			} else if (s.equals("*")) {
				this._cur_len = -1;
				this.flush();
			} else {
				this.flush();
				this._cur_name = s;
				this._cur_len = 1;
			}

		}

		private void flush() {

			final String name = this._cur_name;
			final int len0 = this._cur_len;
			final int off = 0;
			int len = 0;
			String[] dat = null;

			if (len0 < 0) {
				dat = new String[1];
				len = 0;
				dat[0] = name;
			} else if (len0 > 0) {
				dat = new String[len0];
				len = len0;
				dat[0] = name;
			} else {
				this._cur_len = 0;
				this._cur_name = null;
				return;
			}

			PathPart pp = new PathPart(dat, off, len);
			this._pplist.add(pp);

			this._cur_len = 0;
			this._cur_name = null;

		}
	}

}
