package com.boluozhai.snowflake.datatable.pojo;

public class TestingPOJO implements Model {

	private String id;
	private String someData;

	public TestingPOJO() {
		this.someData = this.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSomeData() {
		return someData;
	}

	public void setSomeData(String someData) {
		this.someData = someData;
	}

}
