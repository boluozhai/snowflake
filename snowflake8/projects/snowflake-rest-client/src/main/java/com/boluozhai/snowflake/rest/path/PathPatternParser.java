package com.boluozhai.snowflake.rest.path;

import java.util.ArrayList;
import java.util.List;

public class PathPatternParser {

	public static PathPatternPart[] parse(String pattern) {
		PatternMaker mk = new PatternMaker();
		String[] array = pattern.split("/");
		for (String s : array) {
			mk.append(s);
		}
		mk.flush();
		PathPatternPart[] parts = mk.create();
		return parts;
	}

	private static class PatternMaker {

		// sample pattern string : '~/app/api/?/?/type/?/?/?/id/*'

		private String _cur_name;
		private int _cur_len;
		private List<PathPatternPart> _pplist = new ArrayList<PathPatternPart>();

		private PathPatternPart[] create() {
			List<PathPatternPart> list = this._pplist;
			return list.toArray(new PathPatternPart[list.size()]);
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
			boolean mut_len = false;
			String[] dat = null;

			if (len0 < 0) {
				dat = new String[1];
				len = 1;
				dat[0] = name;
				mut_len = true;
			} else if (len0 > 0) {
				dat = new String[len0];
				len = len0;
				dat[0] = name;
			} else {
				this._cur_len = 0;
				this._cur_name = null;
				return;
			}

			PathPatternPart pp = new PathPatternPart(dat, off, len, mut_len);
			this._pplist.add(pp);

			this._cur_len = 0;
			this._cur_name = null;

		}
	}

}
