package com.boluozhai.snowflake.h2o.data.dao;

import com.boluozhai.snowflake.datatable.DAO;
import com.boluozhai.snowflake.h2o.data.pojo.AccountAlias;

public class AccountAliasDAO implements DAO {

	@Override
	public String getKey(Object object) {
		AccountAlias o2 = (AccountAlias) object;
		return o2.getName();
	}

	@Override
	public void setKey(String key, Object object) {
		AccountAlias o2 = (AccountAlias) object;
		o2.setName(key);
	}

	@Override
	public Object makePrototype(Object object) {
		final AccountAlias o2;
		if (object == null) {
			o2 = new AccountAlias();
		} else {
			o2 = (AccountAlias) object;
		}
		// TODO ...
		return o2;
	}

	@Override
	public String genKey(Object object) {
		AccountAlias o2 = (AccountAlias) object;
		return o2.getName();
	}

}
