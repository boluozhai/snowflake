package com.boluozhai.snowflake.xgit.vfs.impl.refs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.xgit.refs.Ref;
import com.boluozhai.snowflake.xgit.refs.RefManager;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;

final class FileReferManagerImpl implements RefManager {

	public static ComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder extends FileXGitComponentBuilder {

		@Override
		public void configure(ContextBuilder cb) {
		}

		@Override
		public Component create(ComponentContext cc) {
			VPath path = this.getPath();
			return new FileReferManagerImpl(cc, path);
		}

	}

	private static class RefsFinder {

		private final VPath base;
		private final VPath begin;
		private final List<String> results;

		public RefsFinder(VPath base) {
			this.base = base;
			this.begin = base;
			this.results = new ArrayList<String>();
		}

		public RefsFinder(VPath base, VPath begin) {
			this.base = base;
			this.begin = begin;
			this.results = new ArrayList<String>();
		}

		public String[] find() {
			results.clear();
			this.scan(this.base, 10);
			return results.toArray(new String[results.size()]);
		}

		private void scan(VPath node, int limit) {
			if (limit < 0) {
				throw new RuntimeException("too deep");
			}
			VFile file = node.file();
			if (file.isDirectory()) {
				String[] list = file.list();
				for (String name : list) {
					VPath ch = node.child(name);
					this.scan(ch, limit - 1);
				}
			} else {
				this.on_file(node);
			}
		}

		private void on_file(VPath node) {
			List<String> list = new ArrayList<String>();
			VPath p = node;
			for (; p != null; p = p.parent()) {
				list.add(p.name());
				if (p == this.base) {
					break;
				}
			}
			StringBuilder sb = new StringBuilder();
			Collections.reverse(list);
			for (String s : list) {
				if (sb.length() > 0) {
					sb.append('/');
				}
				sb.append(s);
			}
			String name = sb.toString();
			this.results.add(name);
		}

	}

	private class Life implements ComponentLifecycle {

		@Override
		public void onCreate() {
		}

	}

	private static class Inner {

		private final ComponentContext context;
		private final VPath base;
		private final String reg_prefix;

		public Inner(ComponentContext cc, VPath path) {
			this.base = path;
			this.context = cc;
			this.reg_prefix = base.name();
		}

		public String[] name2array(String name) {
			name = name.replace('\\', '/');
			return name.split("/");
		}

		public String[] normal(String[] array) {
			boolean need = false;
			for (int i = array.length - 1; i >= 0; i--) {
				String s = array[i];
				if (s == null) {
					need = true;
				} else {
					s = s.trim();
					if (s.length() == 0) {
						s = null;
						need = true;
					}
					array[i] = s;
				}
			}
			if (need) {
				List<String> list = new ArrayList<String>();
				for (String s : array) {
					if (s == null) {
						continue;
					} else {
						list.add(s);
					}
				}
				array = list.toArray(new String[list.size()]);
			}
			return array;
		}

		public String array2name(String[] array) {
			StringBuilder sb = new StringBuilder();
			for (String s : array) {
				if (sb.length() > 0) {
					sb.append('/');
				}
				sb.append(s);
			}
			return sb.toString();
		}

		public void check_array(String[] array) {

			final int len = array.length;

			if (len < 3) {
				String msg = "the name-path is too short: " + len;
				throw new RuntimeException(msg);
			}

			final String p0 = array[0];

			if (!p0.equals(reg_prefix)) {
				String msg = "the prefix [%s] & name [%s] not match";
				msg = String.format(msg, reg_prefix, p0);
				throw new RuntimeException(msg);
			}

		}

		public Ref make_ref(String name, String[] array) {
			VPath p = this.base;
			for (int i = 1; i < array.length; i++) {
				p = p.child(array[i]);
			}
			return new InnerRef(name, p);
		}

		public String[] find_all() {
			RefsFinder finder = new RefsFinder(base);
			return finder.find();
		}

		public String[] find_in_path(List<String> list) {

			String s1 = list.get(0);
			String s2 = base.name();
			if (!s1.equals(s2)) {
				throw new RuntimeException("bad ref-name: " + list);
			}
			VPath p = base;
			for (int i = 1; i < list.size(); i++) {
				p = p.child(list.get(i));
			}
			RefsFinder finder = new RefsFinder(base, p);
			return finder.find();
		}
	}

	private final Inner inner;

	private FileReferManagerImpl(ComponentContext cc, VPath path) {
		this.inner = new Inner(cc, path);
	}

	@Override
	public ComponentLifecycle lifecycle() {
		return new Life();
	}

	@Override
	public ComponentContext getComponentContext() {
		return inner.context;
	}

	@Override
	public Ref getReference(String name) {
		String[] array = inner.name2array(name);
		array = inner.normal(array);
		name = inner.array2name(array);
		inner.check_array(array);
		return inner.make_ref(name, array);
	}

	@Override
	public String[] list() {
		return this.inner.find_all();
	}

	@Override
	public String[] list(String prefix) {

		prefix = prefix.replace('\\', '/');
		String[] array = prefix.split("/");
		List<String> list = new ArrayList<String>();

		for (String s : array) {
			if (s == null) {
				continue;
			} else {
				s = s.trim();
			}
			if (s.length() < 1) {
				continue;
			} else {
				list.add(s);
			}
		}

		return this.inner.find_in_path(list);
	}

}
