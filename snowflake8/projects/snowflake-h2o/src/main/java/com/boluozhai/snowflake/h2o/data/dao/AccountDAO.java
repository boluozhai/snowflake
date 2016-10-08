package com.boluozhai.snowflake.h2o.data.dao;

import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.h2o.data.pojo.model.AccountDTM;

public class AccountDAO {

	private final DataClient client;

	public AccountDAO(DataClient dc) {
		this.client = dc;
	}

	public AccountDTM getAccount(String uid) {
		AccountDTM model = client.get(uid, AccountDTM.class);
		return model;
	}

}
