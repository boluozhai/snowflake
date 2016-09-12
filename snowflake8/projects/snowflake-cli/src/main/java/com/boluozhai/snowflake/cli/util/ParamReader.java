package com.boluozhai.snowflake.cli.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.context.SnowflakeParameters;

public final class ParamReader {

	public static interface Builder {

		ParamReader create(SnowflakeParameters param);

		void option(String name);

		void option(String name, String addtion);

		void option(String name, int count_addition);

	}

	private static class InnerBuilder implements Builder {

		private Map<String, Option> options;

		private InnerBuilder() {
			this.options = new HashMap<String, Option>();
		}

		@Override
		public ParamReader create(SnowflakeParameters param) {
			return new ParamReader(this, param);
		}

		@Override
		public void option(String name) {
			this.option(name, 0);
		}

		@Override
		public void option(String name, String addition) {
			this.option(name, 1);
		}

		@Override
		public void option(String name, int count_addition) {
			Option option = new Option();
			option.name = name;
			option.count_addition = count_addition;
			this.options.put(name, option);
		}

	}

	private static class Option {
		public String name;
		public int count_addition;

		public String toString() {
			return name;
		}

	}

	public static class Parameter {

		public boolean isOption;
		public String name;
		public String value;
		public List<String> values;

		public String toString() {

			StringBuilder sb = new StringBuilder();

			sb.append("{");
			sb.append("option=").append(this.isOption);
			sb.append(", name=").append(this.name);
			sb.append(", value=").append(this.value);

			List<String> list = this.values;
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					String val = list.get(i);
					sb.append(", value").append(i).append("=").append(val);
				}
			}

			sb.append("}");
			return sb.toString();
		}

	}

	public static Builder newBuilder() {
		return new InnerBuilder();
	}

	private final Map<String, Option> _options;
	private final SnowflakeParameters _param;
	private int _ptr;

	public ParamReader(InnerBuilder builder, SnowflakeParameters param) {
		this._options = builder.options;
		this._param = param;
	}

	public Parameter read() {

		int index = (this._ptr++);
		String val = _param.getParameter(String.valueOf(index), null);
		if (val == null) {
			return null;
		}

		Parameter pa = new Parameter();
		pa.isOption = val.startsWith("-");
		if (pa.isOption) {
			pa.name = val;
			Option op = this._options.get(val);
			if (op != null) {
				int cnt = op.count_addition;
				for (; cnt > 0; cnt--) {
					int i2 = (this._ptr++);
					String v2 = _param.getParameter(String.valueOf(i2), null);
					if (pa.value == null) {
						pa.value = v2;
					} else if (pa.values == null) {
						pa.values = new ArrayList<String>();
						pa.values.add(pa.value);
						pa.values.add(v2);
					} else {
						pa.values.add(v2);
					}
				}
			}
		} else {
			pa.value = val;
		}

		return pa;
	}

}
