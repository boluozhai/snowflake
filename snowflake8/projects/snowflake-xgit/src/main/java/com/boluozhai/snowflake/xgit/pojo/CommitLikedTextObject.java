package com.boluozhai.snowflake.xgit.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommitLikedTextObject {

	public static class Header {

		private String value;
		private List<String> values;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public List<String> getValues() {
			return values;
		}

		public void setValues(List<String> values) {
			this.values = values;
		}

		public int count() {

			int cnt = 0;
			String v1 = this.value;
			List<String> v2 = this.values;

			if (v1 != null) {
				cnt++;
			}

			if (v2 != null) {
				cnt += v2.size();
			}

			return cnt;
		}

		public void add(String newValue) {
			String v1 = this.value;
			List<String> v2 = this.values;
			if (v1 == null) {
				this.value = newValue;
				return;
			}
			if (v2 == null) {
				v2 = new ArrayList<String>();
				this.values = v2;
			}
			v2.add(newValue);
		}

		public void set(String newValue) {
			this.value = newValue;
			this.values = null;
		}

		public String get() {
			String v1 = this.value;
			List<String> v2 = this.values;
			if (v1 != null) {
				return v1;
			} else if (v2 != null) {
				if (v2.size() > 0) {
					return v2.get(0);
				}
			}
			return null;
		}

		public String[] values() {
			String v1 = this.value;
			List<String> v2 = this.values;
			List<String> list = new ArrayList<String>();
			if (v1 != null) {
				list.add(v1);
			}
			if (v2 != null) {
				list.addAll(v2);
			}
			return list.toArray(new String[list.size()]);
		}
	}

	private String type;
	private Map<String, Header> head;
	private String body;

	public CommitLikedTextObject() {
		head = new HashMap<String, Header>();
	}

	public Map<String, Header> getHead() {
		return head;
	}

	public void setHead(Map<String, Header> head) {
		this.head = head;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getHeaderValue(String key) {
		Header header = head.get(key);
		if (header == null) {
			return null;
		} else {
			return header.get();
		}
	}

	public String[] getHeaderValues(String key) {
		Header header = head.get(key);
		if (header == null) {
			return null;
		} else {
			return header.values();
		}
	}

	public void setHeaderValue(String key, String value) {
		Header header = new Header();
		header.set(value);
		head.put(key, header);
	}

	public void addHeaderValue(String key, String value) {
		Header header = head.get(key);
		if (header == null) {
			header = new Header();
			head.put(key, header);
		}
		header.add(value);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
