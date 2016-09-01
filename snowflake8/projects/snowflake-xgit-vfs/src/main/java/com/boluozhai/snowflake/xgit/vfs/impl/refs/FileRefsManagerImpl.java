package com.boluozhai.snowflake.xgit.vfs.impl.refs;

import java.util.ArrayList;
import java.util.List;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.refs.Reference;
import com.boluozhai.snowflake.xgit.vfs.FileRefs;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;

public class FileRefsManagerImpl implements Component, FileRefs {

	public static class Builder extends FileXGitComponentBuilder implements
			ComponentBuilder {

		@Override
		public Component create(ComponentContext cc, ContextBuilder cb) {
			return new FileRefsManagerImpl(this, cc, cb);
		}

	}

	private class MyLife implements ComponentLifecycle {

		@Override
		public void onCreate() {

		}
	}

	private final ComponentContext _context;
	private final VFile _file_refs;
	private final VFile _file_base;

	public FileRefsManagerImpl(Builder builder, ComponentContext cc,
			ContextBuilder cb) {

		VFile file = builder.getPath().file();

		this._context = cc;
		this._file_base = file.getParentFile();
		this._file_refs = file;

	}

	@Override
	public ComponentContext getComponentContext() {
		return this._context;
	}

	@Override
	public ComponentLifecycle lifecycle() {
		return new MyLife();
	}

	@Override
	public Reference getReference(String name) {
		VFile p = this._file_base;
		StringBuilder sb = new StringBuilder();
		char[] chs = name.toCharArray();
		int cnt_slash = 0;
		for (char ch : chs) {
			if (ch == '\\' || ch == '/') {
				p = Helper.childOf(p, sb);
				cnt_slash++;
			} else {
				sb.append(ch);
			}
		}
		if (cnt_slash == 0) {
			name = name.toUpperCase();
			p = p.child(name);
		} else {
			p = Helper.childOf(p, sb);
		}
		return new FileReferenceImpl(p, name);
	}

	@Override
	public Reference findTargetReference(String name) {
		return Helper.findTarget(this, name, 16);
	}

	@Override
	public ObjectId findTargetId(String name) {
		Reference ref = this.findTargetReference(name);
		return ref.getTargetId();
	}

	@Override
	public VFile getFile() {
		return this._file_refs;
	}

	private static class Helper {

		public static Reference findTarget(FileRefsManagerImpl impl,
				String name, int depth_lim) {

			List<String> list = new ArrayList<String>();

			for (int timeout = depth_lim; timeout > 0; timeout--) {

				list.add(name);

				Reference ref = impl.getReference(name);
				if (!ref.exists()) {
					return null;
				}

				if (ref.isId()) {
					return ref;
				} else if (ref.isReferenceName()) {
					name = ref.getTargetReferenceName();
				} else {
					return ref;
				}

			}

			StringBuilder sb = new StringBuilder();
			sb.append("the reference is too deep:\n");
			for (String s : list) {
				sb.append("    ").append(s).append('\n');
			}

			throw new RuntimeException(sb.toString());

		}

		public static VFile childOf(VFile p, StringBuilder sb) {

			final String part = sb.toString().trim();
			sb.setLength(0);
			if (part.length() > 0) {
				p = p.child(part);
			}
			return p;

		}
	}

}
