package com.boluozhai.snowflake.pojo.bridge.config;

import java.util.Map;

import com.boluozhai.snowflake.pojo.bridge.PojoMapper;

public class BridgeMapping {

	private Object from;
	private Object to;
	private Map<String, String> fields; // <from_name,to_name>
	private PojoMapper mapper;

	public PojoMapper getMapper() {
		return mapper;
	}

	public void setMapper(PojoMapper mapper) {
		this.mapper = mapper;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public Object getFrom() {
		return from;
	}

	public void setFrom(Object from) {
		this.from = from;
	}

	public Object getTo() {
		return to;
	}

	public void setTo(Object to) {
		this.to = to;
	}

}
