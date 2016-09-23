package com.boluozhai.snowflake.h2o.data.dao;

import com.boluozhai.snowflake.datatable.DAO;
import com.boluozhai.snowflake.h2o.data.pojo.Auth;
import com.boluozhai.snowflake.util.HashTools;

public class AuthDAO implements DAO {

	@Override
	public String getKey(Object object) {
		Auth o2 = (Auth) object;
		return o2.getId();
	}

	@Override
	public void setKey(String key, Object object) {
		Auth o2 = (Auth) object;
		o2.setId(key);
	}

	@Override
	public Object makePrototype(Object object) {
		Auth o2 = new Auth();
		return (object = o2);
	}

	@Override
	public String genKey(Object object) {
		Auth o2 = (Auth) object;
		String type = o2.getType();
		String key = o2.getAccount().toString();
		return HashTools.sha1string(type + ":" + key);
	}

}
