package com.boluozhai.snowflake.xgit.support;

import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;

/********************************
 * this is a spring BEAN
 * */

public class RepositoryProfile {

	private Map<String, ComponentBuilderFactory> components;
	private Map<String, String> defaultProperties;
	private Map<String, String> finalProperties;
	private List<String> avaliableRepositoryDirectoryNames;

	public Map<String, ComponentBuilderFactory> getComponents() {
		return components;
	}

	public void setComponents(Map<String, ComponentBuilderFactory> components) {
		this.components = components;
	}

	public Map<String, String> getDefaultProperties() {
		return defaultProperties;
	}

	public void setDefaultProperties(Map<String, String> defaultProperties) {
		this.defaultProperties = defaultProperties;
	}

	public Map<String, String> getFinalProperties() {
		return finalProperties;
	}

	public void setFinalProperties(Map<String, String> finalProperties) {
		this.finalProperties = finalProperties;
	}

	public List<String> getAvaliableRepositoryDirectoryNames() {
		return avaliableRepositoryDirectoryNames;
	}

	public void setAvaliableRepositoryDirectoryNames(
			List<String> avaliableRepositoryDirectoryNames) {
		this.avaliableRepositoryDirectoryNames = avaliableRepositoryDirectoryNames;
	}

}
