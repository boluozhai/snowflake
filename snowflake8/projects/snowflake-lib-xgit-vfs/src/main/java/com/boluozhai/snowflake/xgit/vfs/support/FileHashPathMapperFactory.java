package com.boluozhai.snowflake.xgit.vfs.support;

import java.util.ArrayList;
import java.util.List;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.HashId;
import com.boluozhai.snowflake.xgit.vfs.HashPathMapper;

public class FileHashPathMapperFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder implements ComponentBuilder {

		@Override
		public Component create(ComponentContext cc, ContextBuilder cb) {
			return new Com(cc, cb);
		}
	}

	private static class Com implements Component, HashPathMapper {

		private final ComponentContext context;
		public String hash_path_pattern;
		public int[] pattern_cache;

		public Com(ComponentContext cc, ContextBuilder cb) {
			this.context = cc;
		}

		@Override
		public ComponentContext getComponentContext() {
			return this.context;
		}

		@Override
		public ComponentLifecycle lifecycle() {
			return new Life(this);
		}

		@Override
		public VFile getHashPath(VFile base, HashId hash) {
			return this.getHashPath(base, hash, null);
		}

		@Override
		public VFile getHashPath(VFile base, HashId hash, String suffix) {
			final char sep = base.vfs().separatorChar();
			final int[] pattern = this.get_path_pattern();
			final String str = hash.toString();
			final StringBuilder sb = new StringBuilder();
			int i = 0;
			for (int n : pattern) {
				final int i1 = i;
				final int i2 = i + n;
				final String name = str.substring(i1, i2);
				i += n;
				if (sb.length() > 0) {
					sb.append(sep);
				}
				sb.append(name);
			}
			sb.append(sep);
			sb.append(str.substring(i));
			if (suffix != null) {
				sb.append(suffix);
			}
			return base.child(sb.toString());
		}

		private int[] get_path_pattern() {
			int[] cache = this.pattern_cache;
			if (cache == null) {
				cache = PatternCacheBuilder.load(this.hash_path_pattern);
				this.pattern_cache = cache;
			}
			return cache;
		}

	}

	private static class PatternCacheBuilder {

		public static int[] load(String str) {

			final List<Integer> list = new ArrayList<Integer>();
			final char[] chs = str.toCharArray();
			int width = 0;

			for (char ch : chs) {
				switch (ch) {
				case ' ':
				case '\t':
				case 0x0a:
				case 0x0d: {
					// NOP
					break;
				}
				case '/':
				case '\\': {
					// flush
					list.add(width);
					width = 0;
					break;
				}
				default:
					width++;
					break;
				}
			}

			int[] array = new int[list.size()];
			for (int i = array.length - 1; i >= 0; i--) {
				array[i] = list.get(i);
			}
			return array;

		}
	}

	private static class Life implements ComponentLifecycle {

		private final Com com;

		public Life(Com c) {
			this.com = c;
		}

		@Override
		public void onCreate() {

			String pattern = com.context.getProperty("xgit.hashpathpattern");
			com.hash_path_pattern = pattern;

		}
	}

}
