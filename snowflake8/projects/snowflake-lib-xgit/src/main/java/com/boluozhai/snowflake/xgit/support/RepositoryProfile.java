package com.boluozhai.snowflake.xgit.support;

import java.util.List;
import java.util.Map;

import com.boluozhai.snow.mvc.model.ComponentBuilderFactory;

/********************************
 * this is a spring BEAN
 * */

public class RepositoryProfile {

	private Map<String, ComponentBuilderFactory> components;
	private Map<String, String> properties;
	private List<String> avaliableRepositoryDirectoryNames;

	public Map<String, ComponentBuilderFactory> getComponents() {
		return components;
	}

	public void setComponents(Map<String, ComponentBuilderFactory> components) {
		this.components = components;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public List<String> getAvaliableRepositoryDirectoryNames() {
		return avaliableRepositoryDirectoryNames;
	}

	public void setAvaliableRepositoryDirectoryNames(
			List<String> avaliableRepositoryDirectoryNames) {
		this.avaliableRepositoryDirectoryNames = avaliableRepositoryDirectoryNames;
	}

}
