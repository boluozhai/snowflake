package com.boluozhai.snowflake.h2o.data.dao;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.datatable.DAO;
import com.boluozhai.snowflake.h2o.data.pojo.Account;
import com.boluozhai.snowflake.util.HashTools;

public class AccountDAO implements DAO {

	@Override
	public String getKey(Object object) {
		Account o2 = (Account) object;
		return o2.getId();
	}

	@Override
	public void setKey(String key, Object object) {
		Account o2 = (Account) object;
		o2.setId(key);
	}

	@Override
	public Object makePrototype(Object object) {
		final Account o2;
		if (object == null) {
			o2 = new Account();
		} else {
			o2 = (Account) object;
		}

		return o2;
	}

	@Override
	public String genKey(Object object) {
		Account o2 = (Account) object;
		String email = o2.getEmail();
		if (email.indexOf('@') < 0) {
			throw new SnowflakeException("bad email address : " + email);
		}
		return HashTools.sha1string(email);
	}

}
