package com.boluozhai.snowflake.vfs.impl;

import java.util.ArrayList;
import java.util.List;

import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;

final class VPathBuilder {

	private final VFile _file;

	public VPathBuilder(VFile file) {
		this._file = file;
	}

	public VPath create() {

		List<VFile> list = new ArrayList<VFile>();
		int tout = 100;
		VFile p = _file;
		for (; p != null; p = p.getParentFile()) {

			if ((tout--) < 0) {
				throw new RuntimeException("too deep.");
			}

			list.add(p);
		}

		InnerPath parent = null;
		for (int i = list.size() - 1; i >= 0; i--) {
			VFile file = list.get(i);
			InnerPath path = new InnerPath(parent, file);
			parent = path;
		}

		return parent;
	}

	private static class InnerPath implements VPath {

		private final InnerPath _parent;
		private final VFile _file;
		private String _string;
		private final String _name;

		public InnerPath(InnerPath parent, VFile file) {
			this._parent = parent;
			this._file = file;
			this._name = file.getName();
		}

		@Override
		public String toString() {
			String s = this._string;
			if (s == null) {
				s = "path:" + this._file.toString();
				this._string = s;
			}
			return s;
		}

		@Override
		public VFile file() {
			return _file;
		}

		@Override
		public VPath parent() {
			return _parent;
		}

		@Override
		public VPath child(String name) {

			List<String> list = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			for (char ch : name.toCharArray()) {
				if (ch == '\\' || ch == '/') {
					String s = sb.toString();
					sb.setLength(0);
					if (s.length() > 0) {
						list.add(s);
					}
				} else {
					sb.append(ch);
				}
			}
			if (sb.length() > 0) {
				list.add(sb.toString());
			}

			InnerPath p = this;
			for (String s : list) {
				p = p.inner_simple_child(s);
			}
			return p;
		}

		private InnerPath inner_simple_child(String s) {
			VFile f2 = _file.child(s);
			return new InnerPath(this, f2);
		}

		@Override
		public String name() {
			return this._name;
		}

	}

}
