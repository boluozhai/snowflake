package com.boluozhai.snowflake.datatable.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.datatable.mapping.TypeMapping;
import com.boluozhai.snowflake.datatable.mapping.TypeMappingFactory;
import com.boluozhai.snowflake.datatable.mapping.TypeTable;

public class DefaultTypeMappingFactory implements TypeMappingFactory {

	@Override
	public TypeMapping create(TypeTable tt) {
		List<InnerInfo> list = new ArrayList<InnerInfo>();
		Map<String, Object> map = tt.getTypes();
		for (String name : map.keySet()) {
			Class<?> type = map.get(name).getClass();
			InnerInfo info = new InnerInfo(name, type);
			list.add(info);
		}
		return new InnerMapping(list);
	}

	private static class InnerInfo {

		private final Class<?> type;
		private final String name;

		public InnerInfo(String n, Class<?> t) {
			this.name = n.toUpperCase();
			this.type = t;
		}
	}

	private static class InnerMapping implements TypeMapping {

		private final Map<String, InnerInfo> table;
		private final String[] names;

		private InnerMapping(List<InnerInfo> list) {
			List<String> nlist = new ArrayList<String>();
			Map<String, InnerInfo> tab = new HashMap<String, InnerInfo>();
			for (InnerInfo info : list) {
				String k1 = info.name;
				String k2 = info.type.getName();
				tab.put(k1, info);
				tab.put(k2, info);
				nlist.add(k1);
			}
			Collections.sort(nlist);
			this.table = Collections.synchronizedMap(tab);
			this.names = nlist.toArray(new String[nlist.size()]);
		}

		@Override
		public String[] listNames() {
			String[] src = this.names;
			String[] dst = new String[src.length];
			for (int i = dst.length - 1; i >= 0; i--) {
				dst[i] = src[i];
			}
			return dst;
		}

		@Override
		public Class<?> getClass(String name) {
			return this.table.get(name).type;
		}

		@Override
		public String getName(Class<?> type) {
			String key = type.getName();
			return this.table.get(key).name;
		}
	}

}
