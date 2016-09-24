package com.boluozhai.snowflake.datatable.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.datatable.mapping.TypeMappingConfig;
import com.boluozhai.snowflake.datatable.mapping.TypeMappingFactory;
import com.boluozhai.snowflake.datatable.mapping.TypeMappingInfo;
import com.boluozhai.snowflake.datatable.mapping.TypeMappingTable;
import com.boluozhai.snowflake.datatable.pojo.Model;

public class DefaultTypeMappingFactory implements TypeMappingFactory {

	@Override
	public TypeMappingTable create(TypeMappingConfig tt) {
		List<InnerInfo> list = new ArrayList<InnerInfo>();
		Map<String, Model> map = tt.getModels();
		for (String name : map.keySet()) {
			Class<? extends Model> type = map.get(name).getClass();
			InnerInfo info = new InnerInfo(name, type);
			list.add(info);
		}
		return new InnerMapping(list);
	}

	private static class InnerInfo implements TypeMappingInfo {

		private final Class<? extends Model> type;
		private final String name;

		public InnerInfo(String n, Class<? extends Model> t) {
			this.name = n.toUpperCase();
			this.type = t;
		}

		@Override
		public Class<? extends Model> type() {
			return type;
		}

		@Override
		public String name() {
			return name;
		}
	}

	private static class InnerMapping implements TypeMappingTable {

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
		public TypeMappingInfo get(String name) {
			InnerInfo info = this.table.get(name);
			if (info == null) {
				String msg = "undefine type: " + name;
				throw new RuntimeException(msg);
			}
			return info;
		}

		@Override
		public TypeMappingInfo get(Class<? extends Model> type) {
			String key = type.getName();
			return this.get(key);
		}
	}

}
