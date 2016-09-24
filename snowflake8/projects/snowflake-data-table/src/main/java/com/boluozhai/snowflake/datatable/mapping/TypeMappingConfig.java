package com.boluozhai.snowflake.datatable.mapping;

import java.util.Map;

import com.boluozhai.snowflake.datatable.pojo.Model;

public class TypeMappingConfig {

	private Map<String, Model> models;

	public TypeMappingConfig() {
	}

	public Map<String, Model> getModels() {
		return models;
	}

	public void setModels(Map<String, Model> models) {
		this.models = models;
	}

}
