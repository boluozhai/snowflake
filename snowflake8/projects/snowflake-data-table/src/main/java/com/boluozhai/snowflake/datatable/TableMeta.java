package com.boluozhai.snowflake.datatable;

public class TableMeta {

	private String name;
	private Object prototype;
	private DAO dao;

	public TableMeta() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getPrototype() {
		return prototype;
	}

	public void setPrototype(Object prototype) {
		this.prototype = prototype;
	}

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

}
