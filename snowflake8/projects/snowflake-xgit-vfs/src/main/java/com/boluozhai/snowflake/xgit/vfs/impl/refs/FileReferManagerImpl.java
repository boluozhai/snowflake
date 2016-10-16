package com.boluozhai.snowflake.xgit.vfs.impl.refs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.xgit.refs.Ref;
import com.boluozhai.snowflake.xgit.refs.RefManager;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;
import com.boluozhai.snowflake.xgit.vfs.support.FileRefsManagerFactory;

final class FileReferManagerImpl implements RefManager {

	public static ComponentBuilder newBuilder(FileRefsManagerFactory factory) {
		return new Builder(factory);
	}

	private static class Builder extends FileXGitComponentBuilder {

		private List<String> _accept_prefix;

		public Builder(FileRefsManagerFactory factory) {
			this._accept_prefix = factory.getAcceptPrefix();
		}

		@Override
		public void configure(ContextBuilder cb) {
		}

		@Override
		public Component create(ComponentContext cc) {

			List<String> accept = this._accept_prefix;
			if (accept == null) {
				throw new SnowflakeException("need property: acceptPrefix");
			}
			String[] accept_array = accept.toArray(new String[accept.size()]);
			if (accept_array.length == 0) {
				throw new SnowflakeException("need property: acceptPrefix");
			}

			VPath path = this.getPath();
			return new FileReferManagerImpl(cc, path, accept_array);
		}

	}

	private static class RefsFinder {

		private final VPath base;
		private final VPath begin; // = base+offset
		private final List<String> results;

		public RefsFinder(VPath base, VPath begin) {

			this.base = base;
			this.begin = begin;

			// check
			VPath p = begin;
			for (; p != null; p = p.parent()) {
				if (p == base) {
					break;
				}
			}
			if (p == null) {
				String msg = "the begin[path] not a child of base[path]";
				throw new SnowflakeException(msg);
			}

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
		private final VPath refs;
		private final VPath refs_parent;
		private final String[] reg_prefix;

		public Inner(ComponentContext cc, VPath path, String[] accept) {
			this.refs = path;
			this.refs_parent = path.parent();
			this.context = cc;
			this.reg_prefix = accept;
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

		public void check_name(String name) {

			for (String prefix : reg_prefix) {
				if (name.startsWith(prefix)) {
					return;
				}
			}

			String msg = "the prefix [%s] & name [%s] not match";
			msg = String.format(msg, reg_prefix, name);
			throw new RuntimeException(msg);

		}

		public void check_array(String[] array) {
			final int len = array.length;
			if (len < 3) {
				String msg = "the name-path is too short: " + len;
				throw new RuntimeException(msg);
			}
		}

		public Ref make_ref(String name, String[] array) {
			VPath p = this.refs_parent;
			for (String tag : array) {
				p = p.child(tag);
			}
			return new InnerRef(name, p);
		}

		public String[] find_all() {
			RefsFinder finder = new RefsFinder(this.refs_parent, this.refs);
			return finder.find();
		}

		public String[] find_in_path(String name, List<String> list) {
			this.check_name(name);
			VPath p = this.refs_parent;
			for (String tag : list) {
				p = p.child(tag);
			}
			RefsFinder finder = new RefsFinder(this.refs_parent, p);
			return finder.find();
		}
	}

	private final Inner inner;

	private FileReferManagerImpl(ComponentContext cc, VPath path,
			String[] accept_array) {
		this.inner = new Inner(cc, path, accept_array);
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
		inner.check_name(name);
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

		this.inner.check_name(prefix);

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

		return this.inner.find_in_path(prefix, list);
	}

}
