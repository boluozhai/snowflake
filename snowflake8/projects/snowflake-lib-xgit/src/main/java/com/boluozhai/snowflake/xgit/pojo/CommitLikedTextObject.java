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

		public void add(String newValue) {

			String v1 = this.value;
			List<String> v2 = this.values;

			if (v1 == null) {
				if (v2 == null) {
					this.value = newValue;
				} else {
					v2.add(newValue);
				}
			} else {
				if (v2 == null) {
					v2 = new ArrayList<String>();
				}
				v2.add(v1);
				v2.add(newValue);
				this.values = v2;
				this.value = null;
			}

		}

		public void set(String newValue) {
			this.value = newValue;
			this.values = null;
		}

	}

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
			return header.value;
		}
	}

	public List<String> getHeaderValues(String key) {
		Header header = head.get(key);
		if (header == null) {
			return null;
		} else {
			return header.values;
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

}
